// controller
import { useState } from 'react'
import ModeSelection from './ModeSelection'
import QuizScreen from './QuizScreen'
import ResultsScreen from './ResultsScreen'

interface Choice {
	name: string
	imageUrl: string
}

export interface Question {
	pokemonName: string
	audioUrl: string
	imageUrl: string
	choices?: Choice[]
	hint?: string | null
}

type GameMode = 'normal' | 'expert'

export default function QuizCard() {
	const [mode, setMode] = useState<GameMode | null>(null)
	const [question, setQuestion] = useState<Question | null>(null)
	const [score, setScore] = useState(0)
	const [questionCount, setQuestionCount] = useState(0)
	const [showResult, setShowResult] = useState(false)
	const [isLoading, setIsLoading] = useState(false)

	const fetchQuestion = (selectedMode: GameMode) => {
		setIsLoading(true)
		setQuestion(null)
		fetch(`/api/quiz/question?mode=${selectedMode}`)
			.then(res => res.json())
			.then(data => {
				setQuestion(data)
				setIsLoading(false)
			})
			.catch(err => {
				console.error('Failed to fetch question:', err)
				setIsLoading(false)
			})
	}

	const handleModeSelect = (selectedMode: GameMode) => {
		setMode(selectedMode)
		fetchQuestion(selectedMode)
	}

	const handleAnswer = (isCorrect: boolean) => {
		if (isCorrect) {
			setScore(prev => prev + 1)
		}

		// Wait to get new question
		setTimeout(() => {
			if (questionCount + 1 >= 10) {
				setShowResult(true)
			} else {
				setQuestionCount(prev => prev + 1)
				if (mode) fetchQuestion(mode)
			}
		}, 2000) // Time to see result
	}

	const handlePlayAgain = () => {
		setScore(0)
		setQuestionCount(0)
		setShowResult(false)
		setMode(null)
		setQuestion(null)
	}

	if (showResult) {
		return <ResultsScreen score={score} totalQuestions={10} onPlayAgain={handlePlayAgain} />
	}

	if (!mode) {
		return <ModeSelection onModeSelect={handleModeSelect} />
	}

	if (isLoading || !question) {
		return (
			<div className="text-pokemon-border p-8 text-center">
				<p className="text-2xl">Loading next Pokémon...</p>
			</div>
		)
	}

	return (
		<QuizScreen
			question={question}
			mode={mode}
			questionCount={questionCount}
			totalQuestions={10}
			onAnswer={handleAnswer}
		/>
	)
}
