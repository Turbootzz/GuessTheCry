interface ResultsScreenProps {
	score: number
	totalQuestions: number
	onPlayAgain: () => void
}

export default function ResultsScreen({ score, totalQuestions, onPlayAgain }: ResultsScreenProps) {
	const percentage = Math.round((score / totalQuestions) * 100)

	return (
		<div className="w-full max-w-md rounded-lg bg-white p-8 text-center shadow-xl">
			<h2 className="mb-4 text-3xl font-bold text-gray-800">Game Over</h2>
			<div className="mb-8 space-y-2 text-xl">
				<p className="text-gray-600">
					Final Score:{' '}
					<span className="font-bold text-indigo-600">
						{score} / {totalQuestions}
					</span>
				</p>
				<p className="text-gray-600">
					Accuracy: <span className="font-bold text-indigo-600">{percentage}%</span>
				</p>
			</div>
			<button
				className="w-full rounded-lg bg-indigo-600 px-6 py-3 font-semibold text-white transition-colors hover:bg-indigo-700 focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 focus:outline-none"
				onClick={onPlayAgain}
			>
				Play Again
			</button>
		</div>
	)
}
