// controller
import { useState } from 'react'
import ModeSelection from './ModeSelection'
import GenerationSelection from './GenerationSelection'
import QuizScreen from './QuizScreen'
import ResultsScreen from './ResultsScreen'
import { useAuth } from '../context/AuthContext'
import { startGame as apiStartGame, submitAnswer as apiSubmitAnswer } from '../utils/api'

interface Choice {
	name: string
	imageUrl: string
}

export interface Question {
	pokemonName: string
	audioUrl: string
	imageUrl: string
	choices?: Choice[]
	hints?: string[]
}

type GameMode = 'normal' | 'expert'

const GAME_LENGTH = 10

export default function QuizController() {
	const auth = useAuth()

	// Game State
	const [gameId, setGameId] = useState<string | null>(null)
	const [generation, setGeneration] = useState<number | null>(null)
	const [question, setQuestion] = useState<Question | null>(null)
	const [questionIndex, setQuestionIndex] = useState(0)
	const [lastAnswerResult, setLastAnswerResult] = useState<{
		correct: boolean
		correctAnswerPokemonName: string
	} | null>(null)

	// UI State
	const [mode, setMode] = useState<GameMode | null>(null)
	const [showResult, setShowResult] = useState(false)
	const [finalScore, setFinalScore] = useState({ score: 0, totalQuestions: 0 })
	const [isLoading, setIsLoading] = useState(false)
	const [error, setError] = useState<string | null>(null)

	const handleGenerationSelect = (selectedGeneration: number) => {
		setGeneration(selectedGeneration)
	}

	const handleModeSelect = async (selectedMode: GameMode) => {
		if (generation === null) {
			setError('Please select a generation first.')
			return
		}

		setIsLoading(true)
		setError(null)
		try {
			const data = await apiStartGame(selectedMode, generation, auth)
			setMode(selectedMode)
			setGameId(data.gameId)
			setQuestion(data.firstQuestion)
			setQuestionIndex(0)
		} catch (err) {
			setError(err instanceof Error ? err.message : 'Failed to start game.')
		} finally {
			setIsLoading(false)
		}
	}

	const handleAnswer = (userAnswer: string) => {
		if (!gameId) return

		// dont wait for de backend, but backend decides the score

		// send answer to backend
		apiSubmitAnswer(gameId, { questionIndex, userAnswer }, auth)
			.then(data => {
				// show result for 2 seconds
				setLastAnswerResult({
					correct: data.correct,
					correctAnswerPokemonName: data.correctAnswerPokemonName,
				})

				setTimeout(() => {
					setLastAnswerResult(null)

					if (data.nextQuestion) {
						setQuestion(data.nextQuestion)
						setQuestionIndex(prev => prev + 1)
					} else if (data.finalResult) {
						setFinalScore({
							score: data.finalResult.score || 0,
							totalQuestions: GAME_LENGTH,
						})
						setShowResult(true)
					}
				}, 2000)
			})
			.catch(err => {
				setError(err.message || 'An error occurred while submitting a anwser.')
			})
	}

	const handlePlayAgain = () => {
		// Reset all states
		setGameId(null)
		setQuestion(null)
		setQuestionIndex(0)
		setLastAnswerResult(null)
		setGeneration(null)
		setMode(null)
		setShowResult(false)
		setFinalScore({ score: 0, totalQuestions: 0 })
		setError(null)
	}

	// Render logic
	if (error) {
		return <div className="text-red-500">{error}</div>
	}

	if (showResult) {
		return (
			<ResultsScreen
				score={finalScore.score}
				totalQuestions={finalScore.totalQuestions}
				onPlayAgain={handlePlayAgain}
			/>
		)
	}

	if (isLoading) {
		return <div>Loading...</div>
	}

	if (gameId && question) {
		return (
			<QuizScreen
				key={questionIndex} // force re-render for new question
				question={question}
				mode={mode!}
				questionCount={questionIndex}
				totalQuestions={GAME_LENGTH}
				onAnswer={handleAnswer}
				lastAnswerResult={lastAnswerResult} // give answer
			/>
		)
	}

	// begin state
	if (generation === null) {
		return <GenerationSelection onGenerationSelect={handleGenerationSelect} />
	} else {
		return <ModeSelection onModeSelect={handleModeSelect} />
	}
}
