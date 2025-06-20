interface ModeSelectionProps {
	onModeSelect: (mode: 'normal' | 'expert') => void
}

export default function ModeSelection({ onModeSelect }: ModeSelectionProps) {
	return (
		<div className="w-full max-w-md rounded-lg bg-white p-8 text-center shadow-xl">
			<h2 className="mb-6 text-3xl font-bold text-gray-800">Choose Your Mode</h2>
			<div className="flex flex-col justify-center gap-4 sm:flex-row">
				<button
					className="w-full rounded-lg bg-indigo-600 px-6 py-3 font-semibold text-white transition-colors hover:bg-indigo-700 focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 focus:outline-none"
					onClick={() => onModeSelect('normal')}
				>
					Normal Mode
				</button>
				<button
					className="w-full rounded-lg bg-red-600 px-6 py-3 font-semibold text-white transition-colors hover:bg-red-700 focus:ring-2 focus:ring-red-500 focus:ring-offset-2 focus:outline-none"
					onClick={() => onModeSelect('expert')}
				>
					Expert Mode
				</button>
			</div>
		</div>
	)
}
