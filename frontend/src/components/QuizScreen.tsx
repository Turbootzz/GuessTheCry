import { useEffect, useState, useRef, useMemo } from 'react'
import type { Question } from './QuizController'
//import AudioPlayer from './ui/AudioPlayer'
import { usePokemon } from '../context/PokemonContext'

interface QuizScreenProps {
	question: Question
	mode: 'normal' | 'expert'
	questionCount: number
	totalQuestions: number
	onAnswer: (userAnswer: string) => void
	lastAnswerResult: { correct: boolean; correctAnswerPokemonName: string } | null
}

export default function QuizScreen({
	question,
	mode,
	questionCount,
	totalQuestions,
	onAnswer,
	lastAnswerResult,
}: QuizScreenProps) {
	const [guess, setGuess] = useState('')
	const [hintIndex, setHintIndex] = useState(0)
	const audioRef = useRef<HTMLAudioElement>(null)
	const { pokemonNames } = usePokemon() // for auto-completion

	// play audio when question changes
	useEffect(() => {
		setGuess('')
		setHintIndex(0)
		if (audioRef.current) {
			audioRef.current.src = `${question.audioUrl}?t=${Date.now()}`
			audioRef.current.play().catch(e => console.error('Audio play failed:', e))
		}
	}, [question])

	// useMemo will update suggestions only when guess changes
	const suggestions = useMemo(() => {
		if (!guess || guess.length < 2) {
			return [] // no suggestion
		}
		return pokemonNames
			.filter((name: string) => name.toLowerCase().startsWith(guess.toLowerCase()))
			.slice(0, 5) // max 5 suggestions
	}, [guess, pokemonNames])

	const handleSuggestionClick = (name: string) => {
		setGuess(name) // fill input with suggestion
		handleAnswerSubmit(name) // instant submit
	}

	const handleAnswerSubmit = (answer: string) => {
		if (lastAnswerResult) return // prevent dublicate submissions
		onAnswer(answer)
	}

	const progressPercentage = ((questionCount + 1) / totalQuestions) * 100

	return (
		<div className="w-full max-w-lg rounded-lg bg-white p-8 text-center shadow-xl transition-all">
			{/* Header */}
			<div className="mb-6">
				<p className="text-sm font-semibold text-emerald-700">
					Question {questionCount + 1} of {totalQuestions}
				</p>
				<div className="mt-2 h-2.5 w-full rounded-full bg-gray-200">
					<div
						className="h-2.5 rounded-full bg-emerald-700"
						style={{ width: `${progressPercentage}%` }}
					></div>
				</div>
			</div>

			<h2 className="mb-4 text-2xl font-bold text-gray-800">Guess the Pokémon Cry!</h2>
			<audio ref={audioRef} controls className="mb-6 w-full" />
			{/* <AudioPlayer audioUrl={question.audioUrl} ref={audioRef} /> */}

			{/* Answer and Result section */}
			<div className="min-h-[200px]">
				{lastAnswerResult ? (
					// Show result of the last answer
					<div className="animate-fade-in flex flex-col items-center justify-center">
						<img
							src={question.imageUrl}
							alt={lastAnswerResult.correctAnswerPokemonName}
							className="mb-4 h-32 w-32 object-contain"
						/>
						<p
							className={`text-2xl font-bold ${lastAnswerResult.correct ? 'text-green-600' : 'text-red-600'}`}
						>
							{lastAnswerResult.correct ? 'Correct!' : 'Incorrect!'}
						</p>
						<p className="mt-1 text-xl text-gray-700">
							It was {lastAnswerResult.correctAnswerPokemonName}!
						</p>
					</div>
				) : mode === 'normal' && question.choices ? (
					<div className="grid grid-cols-2 gap-4">
						{question.choices.map((choice, i) => (
							<button
								key={i}
								onClick={() => handleAnswerSubmit(choice.name)}
								className="group rounded-lg bg-gray-100 p-3 transition-colors hover:bg-indigo-100 focus:ring-2 focus:ring-indigo-500 focus:outline-none"
							>
								<img
									src={choice.imageUrl}
									alt={choice.name}
									className="mx-auto h-24 w-24 object-contain transition-transform group-hover:scale-110"
								/>
								<p className="mt-2 font-semibold text-gray-700">{choice.name}</p>
							</button>
						))}
					</div>
				) : (
					<form
						onSubmit={e => {
							e.preventDefault()
							handleAnswerSubmit(guess)
						}}
					>
						<div className="relative mb-4">
							<input
								type="text"
								value={guess}
								onChange={e => setGuess(e.target.value)}
								placeholder="Enter Pokémon name..."
								className="mb-4 w-full rounded-lg border border-gray-300 px-4 py-3 text-center focus:ring-2 focus:ring-indigo-500 focus:outline-none"
								autoFocus
								autoComplete="off" // disable browser autocomplete
							/>
							{/* suggestions */}
							{suggestions.length > 0 && (
								<div className="absolute top-full right-0 left-0 z-10 mt-1 rounded-md border border-gray-200 bg-white text-left shadow-lg">
									<ul>
										{suggestions.map(name => (
											<li
												key={name}
												onClick={() => handleSuggestionClick(name)}
												className="cursor-pointer px-4 py-2 text-left hover:bg-gray-100"
											>
												{name.charAt(0).toUpperCase() + name.slice(1)}
											</li>
										))}
									</ul>
								</div>
							)}
						</div>
						<button
							type="submit"
							className="w-full rounded-lg bg-emerald-700 py-3 font-semibold text-white transition-colors hover:bg-emerald-800"
						>
							Submit Guess
						</button>
					</form>
				)}
			</div>
			{/* Hints section */}
			{mode === 'expert' && (
				<div className="mt-6 min-h-[60px] text-left">
					{/* only show hints when there is no answer */}
					{!lastAnswerResult &&
						Array.isArray(question.hints) &&
						question.hints.length > 0 && (
							<>
								{hintIndex > 0 && (
									<p className="mb-2 font-semibold text-gray-700">Hints:</p>
								)}

								<ul className="mb-2 list-inside list-disc text-gray-600">
									{question.hints.slice(0, hintIndex).map((hint, index) => (
										<li key={index}>💡 {hint}</li>
									))}
								</ul>

								{hintIndex < question.hints.length && (
									<button
										onClick={() => setHintIndex(hintIndex + 1)}
										className="rounded bg-yellow-500 px-3 py-2 text-sm font-semibold text-white hover:bg-yellow-600"
									>
										Show Hint {hintIndex + 1}
									</button>
								)}
							</>
						)}
				</div>
			)}
		</div>
	)
}
