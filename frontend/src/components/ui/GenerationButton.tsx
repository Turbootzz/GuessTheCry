export default function GenerationButton({
	name,
	imageUrl,
	onClick,
}: {
	name: string
	imageUrl: string
	onClick: () => void
}) {
	return (
		<button
			onClick={onClick}
			style={{
				backgroundImage: `url(${imageUrl})`,
				backgroundSize: 'cover',
				backgroundPosition: 'center',
			}}
			className="group relative flex h-24 flex-grow basis-56 items-center justify-center overflow-hidden rounded-lg font-bold text-white shadow-lg transition-all duration-300 hover:scale-105"
		>
			<div className="absolute inset-0 bg-black/50 transition-all duration-300 group-hover:bg-black/40"></div>
			<span className="relative z-10 text-xl drop-shadow-md">{name}</span>
		</button>
	)
}
