export const API_URL = 'http://localhost:8080/api' // backend URL

type Auth = {
	token: string | null
	logout: () => void
}

// Wrapper for fetch
async function apiFetch(endpoint: string, auth: Auth, options: RequestInit = {}) {
	const { token, logout } = auth

	const headers = new Headers(options.headers)
	headers.set('Content-Type', 'application/json')

	if (token) {
		headers.set('Authorization', `Bearer ${token}`)
	}

	const res = await fetch(`${API_URL}${endpoint}`, { ...options, headers })

	// If token is exprired or user is not authorized, log out
	if (res.status === 401 || res.status === 403) {
		logout()
		throw new Error('User is not authorized')
	}

	if (!res.ok) {
		const errorData = await res.json().catch(() => ({ message: 'An unknown error occurred' }))
		throw new Error(errorData.message)
	}

	// If response body is empty, return null
	const text = await res.text()
	return text ? JSON.parse(text) : null
}

export async function login(username: string, password: string) {
	const res = await fetch(`${API_URL}/auth/login`, {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify({ username, password }),
	})

	if (!res.ok) throw new Error('Login failed')
	return res.json()
}

export async function register(username: string, password: string) {
	const res = await fetch(`${API_URL}/auth/register`, {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify({ username, password }),
	})

	if (!res.ok) {
		// try to read error from backend
		const errorData = await res.json().catch(() => ({ message: 'Registration failed' }))
		throw new Error(errorData.message || 'Registration failed')
	}
	return res.json()
}

export function fetchQuestion(mode: string, auth: Auth) {
	return apiFetch(`/quiz/question?mode=${mode}`, auth, { method: 'GET' })
}

export function fetchUserStats(userId: number, auth: Auth) {
	return apiFetch(`/auth/${userId}/stats`, auth, { method: 'GET' })
}

// -- game-related functions
export function startGame(mode: string, auth: Auth) {
	return apiFetch(`/game/start?mode=${mode}`, auth, { method: 'POST' })
}

export function submitAnswer(
	gameId: string,
	answer: { questionIndex: number; userAnswer: string },
	auth: Auth
) {
	return apiFetch(`/game/${gameId}/answer`, auth, {
		method: 'POST',
		body: JSON.stringify(answer),
	})
}
