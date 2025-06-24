import { useNavigate } from 'react-router-dom'

interface ResultsScreenProps {
	score: number
	totalQuestions: number
	onPlayAgain: () => void
}

export default function ResultsScreen({ score, totalQuestions, onPlayAgain }: ResultsScreenProps) {
	const percentage = Math.round((score / totalQuestions) * 100)
	const navigate = useNavigate()
	return (
		<div className="w-full max-w-md rounded-lg bg-white p-8 text-center shadow-xl">
			<h2 className="mb-4 text-3xl font-bold text-gray-800">Game Over</h2>
			<div className="mb-8 space-y-2 text-xl">
				<p className="text-gray-600">
					Final Score:{' '}
					<span className="font-bold text-emerald-700">
						{score} / {totalQuestions}
					</span>
				</p>
				<p className="text-gray-600">
					Accuracy: <span className="font-bold text-emerald-700">{percentage}%</span>
				</p>
			</div>
			<div className="mt-6 space-y-4">
				<button
					className="w-full rounded-lg bg-emerald-700 px-6 py-3 font-semibold text-white shadow-md transition-colors hover:bg-emerald-800 focus:ring-2 focus:ring-emerald-600 focus:ring-offset-2 focus:outline-none"
					onClick={onPlayAgain}
				>
					Play Again
				</button>
				<button
					className="w-full rounded-lg bg-sky-600 px-6 py-3 font-semibold text-white shadow-md transition-colors hover:bg-sky-700 focus:ring-2 focus:ring-sky-500 focus:ring-offset-2 focus:outline-none"
					onClick={() => navigate('/profile')}
				>
					Go to Profile
				</button>
			</div>
		</div>
	)
}
