import GenerationButton from './ui/GenerationButton'

interface GenerationSelectionProps {
	onGenerationSelect: (generation: number) => void
}

const generations = [
	{ number: 1, name: 'Generation I', bgImage: '/gen-backgrounds/gen1.jpg' },
	{ number: 2, name: 'Generation II', bgImage: '/gen-backgrounds/gen2.avif' },
	{ number: 3, name: 'Generation III', bgImage: '/gen-backgrounds/gen3.avif' },
	{ number: 4, name: 'Generation IV', bgImage: '/gen-backgrounds/gen4.jpg' },
	{ number: 5, name: 'Generation V', bgImage: '/gen-backgrounds/gen5.avif' },
	{ number: 6, name: 'Generation VI', bgImage: '/gen-backgrounds/gen6.avif' },
	{ number: 7, name: 'Generation VII', bgImage: '/gen-backgrounds/gen7.avif' },
	{ number: 8, name: 'Generation VIII', bgImage: '/gen-backgrounds/gen8.jpg' },
	{ number: 9, name: 'Generation IX', bgImage: '/gen-backgrounds/gen9.webp' },
]

const allGenerationsOption = { number: 0, name: 'All Generations' }

export default function GenerationSelection({ onGenerationSelect }: GenerationSelectionProps) {
	return (
		<div className="w-full max-w-2xl rounded-lg bg-white p-8 text-center shadow-xl">
			<h2 className="mb-8 text-3xl font-bold text-gray-800">Select a Generation</h2>
			<div className="flex flex-wrap justify-center gap-5">
				{/* all Generations */}
				<button
					onClick={() => onGenerationSelect(allGenerationsOption.number)}
					className="flex h-24 flex-grow basis-56 items-center justify-center rounded-lg bg-indigo-600 px-4 py-3 text-xl font-bold text-white shadow-lg transition-all hover:scale-105 hover:bg-indigo-700"
				>
					{allGenerationsOption.name}
				</button>

				{/* specific gen */}
				{generations.map(gen => (
					<GenerationButton
						key={gen.number}
						name={gen.name}
						imageUrl={gen.bgImage}
						onClick={() => onGenerationSelect(gen.number)}
					/>
				))}
			</div>
		</div>
	)
}
