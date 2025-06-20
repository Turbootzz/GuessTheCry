import { useEffect, useState, useRef } from 'react'
import type { Question } from './QuizController'

interface QuizScreenProps {
	question: Question
	mode: 'normal' | 'expert'
	questionCount: number
	totalQuestions: number
	onAnswer: (isCorrect: boolean) => void
}

export default function QuizScreen({
	question,
	mode,
	questionCount,
	totalQuestions,
	onAnswer,
}: QuizScreenProps) {
	const [result, setResult] = useState<'correct' | 'incorrect' | null>(null)
	const [guess, setGuess] = useState('')
	const audioRef = useRef<HTMLAudioElement>(null)

	useEffect(() => {
		setResult(null)
		setGuess('')
		if (audioRef.current) {
			audioRef.current.src = `${question.audioUrl}?t=${Date.now()}`
			audioRef.current.play().catch(e => console.error('Audio play failed:', e))
		}
	}, [question])

	const handleCheckAnswer = (answer: string) => {
		if (result) return
		const isCorrect = answer.toLowerCase() === question.pokemonName.toLowerCase()
		setResult(isCorrect ? 'correct' : 'incorrect')
		onAnswer(isCorrect)
	}

	const progressPercentage = ((questionCount + 1) / totalQuestions) * 100

	return (
		<div className="w-full max-w-lg rounded-lg bg-white p-8 text-center shadow-xl transition-all">
			{/* Header */}
			<div className="mb-6">
				<p className="text-sm font-semibold text-indigo-600">
					Question {questionCount + 1} of {totalQuestions}
				</p>
				<div className="mt-2 h-2.5 w-full rounded-full bg-gray-200">
					<div
						className="h-2.5 rounded-full bg-indigo-600"
						style={{ width: `${progressPercentage}%` }}
					></div>
				</div>
			</div>

			<h2 className="mb-4 text-2xl font-bold text-gray-800">Guess the Pokémon Cry!</h2>

			<audio
				ref={audioRef}
				src={`${question.audioUrl}?t=${Date.now()}`}
				controls
				className="mb-6 w-full"
			/>

			{/* Answer section */}
			<div className="min-h-[200px]">
				{!result ? (
					mode === 'normal' && question.choices ? (
						<div className="grid grid-cols-2 gap-4">
							{question.choices.map((choice, i) => (
								<button
									key={i}
									onClick={() => handleCheckAnswer(choice.name)}
									className="group rounded-lg bg-gray-100 p-3 transition-colors hover:bg-indigo-100 focus:ring-2 focus:ring-indigo-500 focus:outline-none"
								>
									<img
										src={choice.imageUrl}
										alt={choice.name}
										className="mx-auto h-24 w-24 object-contain transition-transform group-hover:scale-110"
									/>
									<p className="mt-2 font-semibold text-gray-700">
										{choice.name}
									</p>
								</button>
							))}
						</div>
					) : (
						<form
							onSubmit={e => {
								e.preventDefault()
								handleCheckAnswer(guess)
							}}
						>
							<input
								type="text"
								value={guess}
								onChange={e => setGuess(e.target.value)}
								placeholder="Enter Pokémon name..."
								className="mb-4 w-full rounded-lg border border-gray-300 px-4 py-3 text-center focus:ring-2 focus:ring-indigo-500 focus:outline-none"
								autoFocus
							/>
							<button
								type="submit"
								className="w-full rounded-lg bg-indigo-600 py-3 font-semibold text-white transition-colors hover:bg-indigo-700"
							>
								Submit Guess
							</button>
						</form>
					)
				) : (
					// Resultaat
					<div className="flex flex-col items-center justify-center">
						<img
							src={question.imageUrl}
							alt={question.pokemonName}
							className="mb-4 h-32 w-32 object-contain"
						/>
						<p
							className={`text-2xl font-bold ${result === 'correct' ? 'text-green-600' : 'text-red-600'}`}
						>
							{result === 'correct' ? 'Correct!' : 'Incorrect!'}
						</p>
						<p className="mt-1 text-xl text-gray-700">It was {question.pokemonName}!</p>
					</div>
				)}
			</div>
		</div>
	)
}
