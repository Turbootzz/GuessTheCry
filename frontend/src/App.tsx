import { useEffect, useState } from 'react'
import './assets/css/global.css'

interface Question {
	pokemonName: string
	audioUrl: string
	choices?: string[]
}

type GameMode = 'normal' | 'expert'

function QuizCard() {
	const [mode, setMode] = useState<GameMode | null>(null)
	const [pokemon, setPokemon] = useState<Question | null>(null)
	const [guess, setGuess] = useState('')
	const [hint, setHint] = useState<string | null>(null)
	const [result, setResult] = useState<string | null>(null)
	const [score, setScore] = useState(0)
	const [questionCount, setQuestionCount] = useState(0)
	const [showResult, setShowResult] = useState(false)

	const fetchQuestion = () => {
		fetch('/api/quiz/question')
			.then(res => res.json())
			.then(data => {
				setPokemon(data)
				setGuess('')
				setHint(null)
				setResult(null)
			})
	}

	useEffect(() => {
		if (mode) fetchQuestion()
	}, [mode])

	const checkAnswer = (answer: string) => {
		const isCorrect = answer.toLowerCase() === pokemon?.pokemonName.toLowerCase()
		setResult(isCorrect ? '✅ Correct!' : '❌ Incorrect!')
		if (isCorrect) setScore(prev => prev + 1)

		setTimeout(() => {
			if (questionCount + 1 >= 10) {
				setShowResult(true)
			} else {
				setQuestionCount(prev => prev + 1)
				fetchQuestion()
			}
		}, 1000)
	}

	const getHint = () => {
		fetch('/api/quiz/hint')
			.then(res => res.json())
			.then(data => setHint(data.hint))
	}

	if (!mode) {
		return (
			<div className="bg-white rounded-lg shadow-lg p-8 w-full max-w-md text-center">
				<h2 className="text-2xl font-bold text-indigo-600 mb-4">Choose a Mode</h2>
				<div className="flex justify-center gap-4">
					<button
						className="bg-indigo-500 text-white px-4 py-2 rounded hover:bg-indigo-600"
						onClick={() => setMode('normal')}
					>
						Normal Mode
					</button>
					<button
						className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
						onClick={() => setMode('expert')}
					>
						Hard Mode
					</button>
				</div>
			</div>
		)
	}

	if (showResult) {
		const percentage = Math.round((score / 10) * 100)
		return (
			<div className="bg-white rounded-lg shadow-lg p-8 w-full max-w-md text-center">
				<h2 className="text-2xl font-semibold text-indigo-600 mb-4">Game Over</h2>
				<p className="text-xl">Score: {score} / 10</p>
				<p className="text-lg text-gray-700">Accuracy: {percentage}%</p>
				<button
					className="mt-4 bg-indigo-500 text-white px-4 py-2 rounded hover:bg-indigo-600"
					onClick={() => {
						setScore(0)
						setQuestionCount(0)
						setShowResult(false)
						fetchQuestion()
					}}
				>
					Play Again
				</button>
			</div>
		)
	}

	if (!pokemon) return <p className="text-center text-gray-500">Loading...</p>

	return (
		<div className="bg-white rounded-lg shadow-lg p-8 w-full max-w-md text-center">
			<h2 className="text-2xl font-semibold text-indigo-600 mb-4">
				Question {questionCount + 1} / 10
			</h2>
			<audio controls className="mx-auto mb-4">
				<source src={pokemon.audioUrl} type="audio/ogg" />
				Your browser does not support the audio element.
			</audio>

			{mode === 'normal' && pokemon.choices ? (
				<div className="grid grid-cols-2 gap-4 mb-4">
					{pokemon.choices.map((choice, i) => (
						<button
							key={i}
							onClick={() => checkAnswer(choice)}
							className="bg-indigo-100 hover:bg-indigo-300 text-indigo-700 font-semibold py-2 rounded"
							disabled={!!result}
						>
							{choice}
						</button>
					))}
				</div>
			) : (
				<>
					<input
						type="text"
						placeholder="Enter Pokémon name"
						value={guess}
						onChange={e => setGuess(e.target.value)}
						className="w-full px-4 py-2 border border-gray-300 rounded mb-4 focus:outline-none focus:ring-2 focus:ring-indigo-400"
						disabled={!!result}
					/>
					<div className="flex gap-4 justify-center mb-4">
						<button
							onClick={() => checkAnswer(guess)}
							className="bg-indigo-500 text-white px-4 py-2 rounded hover:bg-indigo-600"
							disabled={!!result}
						>
							Submit
						</button>
						<button
							onClick={getHint}
							className="bg-yellow-500 text-white px-4 py-2 rounded hover:bg-yellow-600"
						>
							Hint
						</button>
					</div>
				</>
			)}

			{hint && (
				<p className="text-sm text-gray-700 mb-2">
					<strong>Hint:</strong> {hint}
				</p>
			)}
			{result && (
				<p
					className={`text-lg font-bold ${
						result.includes('Correct') ? 'text-green-600' : 'text-red-600'
					}`}
				>
					{result}
				</p>
			)}
		</div>
	)
}

function App() {
	return (
		<div className="min-h-screen bg-gradient-to-br from-indigo-100 to-white flex items-center justify-center p-4">
			<QuizCard />
		</div>
	)
}

export default App
