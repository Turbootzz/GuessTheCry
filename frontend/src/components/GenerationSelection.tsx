interface GenerationSelectionProps {
	onGenerationSelect: (generation: number) => void
}

const generations = [
	{ number: 1, name: 'Generation I' },
	{ number: 2, name: 'Generation II' },
	{ number: 3, name: 'Generation III' },
	{ number: 4, name: 'Generation IV' },
	{ number: 5, name: 'Generation V' },
	{ number: 6, name: 'Generation VI' },
	{ number: 7, name: 'Generation VII' },
	{ number: 8, name: 'Generation VIII' },
	{ number: 9, name: 'Generation IX' },
]
const allGenerationsOption = { number: 0, name: 'All Generations' }

export default function GenerationSelection({ onGenerationSelect }: GenerationSelectionProps) {
	return (
		<div className="w-full max-w-lg rounded-lg bg-white p-8 text-center shadow-xl">
			<h2 className="mb-6 text-3xl font-bold text-gray-800">Select a Generation</h2>
			<div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
				{/* all generations" */}
				<button
					onClick={() => onGenerationSelect(allGenerationsOption.number)}
					className="rounded-lg bg-indigo-600 px-4 py-3 font-semibold text-white transition-all hover:scale-105 hover:bg-indigo-700"
				>
					{allGenerationsOption.name}
				</button>

				{/* specific gen */}
				{generations.map(gen => (
					<button
						key={gen.number}
						onClick={() => onGenerationSelect(gen.number)}
						className="rounded-lg bg-gray-200 px-4 py-3 font-semibold text-gray-800 transition-all hover:scale-105 hover:bg-gray-300"
					>
						{gen.name}
					</button>
				))}
			</div>
		</div>
	)
}
