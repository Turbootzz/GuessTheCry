import got from "got";
import fs from "fs";
import path from "path";
import AWS from "aws-sdk";
import dotenv from "dotenv";

dotenv.config({ path: "../../backend/.env" });

const BUCKET = "pokemon-cries";
const SPRITE_BUCKET = "pokemon-sprites";
const OUT_JSON = "pokemon-data.json";
const OUT_AUDIO_DIR = "./audio";
const OUT_SPRITE_DIR = "./sprites";
const MAX_ID = 1025; // Change to maximum Pokémon ID that we want to fetch

const showdownNameMap = {
  "nidoran-f": "nidoranf",
  "nidoran-m": "nidoranm",
  "mr-mime": "mrmime",
  "mime-jr": "mimejr",
  "farfetchd": "farfetchd",
  "type-null": "typenull",
  "jangmo-o": "jangmoo",
  "hakamo-o": "hakamoo",
  "kommo-o": "kommoo",
  "ho-oh": "hooh",
  "tapu-koko": "tapukoko",
  "tapu-lele": "tapulele",
  "tapu-bulu": "tapubulu",
  "tapu-fini": "tapufini",
  "great-tusk": "greattusk",
  "scream-tail": "screamtail",
  "brute-bonnet": "brutebonnet",
  "flutter-mane": "fluttermane",
  "slither-wing": "slitherwing",
  "sandy-shocks": "sandyshocks",
  "iron-treads": "irontreads",
  "iron-bundle": "ironbundle",
  "iron-hands": "ironhands",
  "iron-jugulis": "ironjugulis",
  "iron-moth": "ironmoth",
  "iron-thorns": "ironthorns",
  "wo-chien": "wochien",
  "chien-pao": "chienpao",
  "ting-lu": "tinglu",
  "chi-yu": "chiyu",
  "roaring-moon": "roaringmoon",
  "iron-valiant": "ironvaliant",
  "walking-wake": "walkingwake",
  "iron-leaves": "ironleaves",
  "gouging-fire": "gougingfire",
  "raging-bolt": "ragingbolt",
  "iron-boulder": "ironboulder",
  "iron-crown": "ironcrown",
  "mr-rime": "mrrime",
  // maybe add more mappings if needed
};

const s3 = new AWS.S3({
  endpoint: process.env.S3_ENDPOINT,
  accessKeyId: process.env.S3_ACCESS_KEY,
  secretAccessKey: process.env.S3_SECRET_KEY,
  s3ForcePathStyle: true,
  signatureVersion: "v4",
  region: "eu-central-1",
});

(async () => {
  const data = [];
  if (!fs.existsSync(OUT_AUDIO_DIR)) fs.mkdirSync(OUT_AUDIO_DIR);
  if (!fs.existsSync(OUT_SPRITE_DIR)) fs.mkdirSync(OUT_SPRITE_DIR);

  // check if sprites bucket exists, if not create it
  try {
	await s3.headBucket({ Bucket: SPRITE_BUCKET }).promise();
	console.log(`Bucket "${SPRITE_BUCKET}" already exists.`);
  } catch (err) {
	console.log(`Bucket "${SPRITE_BUCKET}" not found. Creating...`);
	await s3.createBucket({ Bucket: SPRITE_BUCKET }).promise();
	// make the bucket public read
	await s3
	  .putBucketPolicy({
		Bucket: SPRITE_BUCKET,
		Policy: JSON.stringify({
		  Version: "2012-10-17",
		  Statement: [
			{
			  Sid: "PublicReadGetObject",
			  Effect: "Allow",
			  Principal: "*",
			  Action: "s3:GetObject",
			  Resource: `arn:aws:s3:::${SPRITE_BUCKET}/*`,
			},
		  ],
		}),
	  })
	  .promise();
	console.log(`Bucket "${SPRITE_BUCKET}" created and set to public read.`);
  }

  for (let id = 1; id <= MAX_ID; id++) {
	try {
	  console.log(`\n--- Processing Pokémon ID: ${id} ---`);
	  const res = await got(`https://pokeapi.co/api/v2/pokemon/${id}`, {
		responseType: "json",
	  });
	  const speciesRes = await got(
		`https://pokeapi.co/api/v2/pokemon-species/${id}`,
		{ responseType: "json" }
	  );

	  const json = res.body;
	  const speciesJson = speciesRes.body;
	  const name = json.name;

	  const generationName = speciesJson.generation.name; // for example 'generation-iii'
	  const generationMap = {
		"generation-i": "gen1",
		"generation-ii": "gen2",
		"generation-iii": "gen3",
		"generation-iv": "gen4",
		"generation-v": "gen5",
		"generation-vi": "gen6",
		"generation-vii": "gen7",
		"generation-viii": "gen8",
		"generation-ix": "gen9",
	  };
	  const generation = generationMap[generationName] || "unknown";

	  // name with '-' removed for showdown compatibility
	  const baseName = name.split("-")[0];
	  const normalizedName = baseName.replace(/-/g, "");
	  // removes pokemon variants like deoxys-normal
	  const showdownName =
		showdownNameMap[name] || showdownNameMap[baseName] || normalizedName;
	  const audioUrl = `https://play.pokemonshowdown.com/audio/cries/${showdownName}.mp3`;
	  const audioPath = `${generation}/${id}.mp3`;
	  const localPath = path.join(OUT_AUDIO_DIR, `${id}.mp3`);

	  try {
		const audioRes = await got(audioUrl, {
		  headers: { "User-Agent": "GuessTheCryBot/1.0" },
		  responseType: "buffer",
		  followRedirect: true,
		});
		const buffer = audioRes.body;
		fs.writeFileSync(localPath, buffer);

		await s3
		  .putObject({
			Bucket: BUCKET,
			Key: audioPath,
			Body: buffer,
			ContentType: "audio/mpeg",
		  })
		  .promise();

		console.log(`✔️ Uploaded: ${audioPath} (from ${name})`);

		data.push({
		  name: name,
		  generation: generation,
		  audioPath: audioPath,
		  pokedexId: id,
		  hints: [], // Add hints if needed
		});
	  } catch (err) {
		console.log(`⚠️ No audio for ${name}`);
		continue;
	  }

	  // download sprite and upload
	  const spriteUrl = `https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${id}.png`;
	  const spritePath = `${generation}/${id}.png`; 
	  const localSpritePath = path.join(OUT_SPRITE_DIR, `${generation}/${id}.png`);

	  try {
		const spriteRes = await got(spriteUrl, {
		  responseType: "buffer",
		});
		const spriteBuffer = spriteRes.body;
		// Ensure the directory exists
		fs.mkdirSync(path.dirname(localSpritePath), { recursive: true });
		fs.writeFileSync(localSpritePath, spriteBuffer);

		await s3
		  .putObject({
			Bucket: SPRITE_BUCKET,
			Key: spritePath,
			Body: spriteBuffer,
			ContentType: "image/png",
		  })
		  .promise();

		console.log(`✔️ Sprite Uploaded: ${spritePath} (for ${name})`);
	  } catch (err) {
		console.log(`⚠️ No sprite for ${name}`);
	  }
	} catch (e) {
	  console.log(`❌ Error with Pokémon ID ${id}:`, e.message);
	}
  }

  fs.writeFileSync(OUT_JSON, JSON.stringify(data, null, 2));
  console.log(`\n✅ Exported ${data.length} Pokémon to ${OUT_JSON}`);
})();
