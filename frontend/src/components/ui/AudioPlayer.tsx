export default function AudioPlayer({ audioUrl }: { audioUrl: string }) {
	return (
		<div className="mb-6">
			<audio controls className="w-full rounded-lg bg-gray-100 p-2 shadow-md" src={audioUrl}>
				Your browser does not support the audio element.
			</audio>
		</div>
	)
}
