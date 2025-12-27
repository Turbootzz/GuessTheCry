import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { register } from '../utils/api'

export default function Register() {
	const [username, setUsername] = useState('')
	const [password, setPassword] = useState('')
	const [error, setError] = useState<string | null>(null)
	const navigate = useNavigate()

	const handleSubmit = async (e: React.FormEvent) => {
		e.preventDefault()
		setError(null) // reset error state

		if (password.length < 6) {
			setError('Password must be at least 6 characters long.')
			return
		}

		try {
			await register(username, password)
			// After successful registration, redirect to login page
			alert('Registration successful! Please log in.')
			navigate('/login')
		} catch (err) {
			setError(err instanceof Error ? err.message : 'An unexpected error occurred.')
		}
	}

	return (
		<div className="mx-auto mt-20 w-full max-w-md">
			<form onSubmit={handleSubmit} className="rounded-lg bg-white p-8 shadow-xl">
				<h2 className="mb-6 text-center text-3xl font-bold text-gray-800">
					Create Account
				</h2>

				{error && (
					<div className="mb-4 rounded-md bg-red-100 p-3 text-center text-sm font-semibold text-red-700">
						{error}
					</div>
				)}

				<div className="mb-4">
					<label className="mb-2 block font-semibold text-gray-700">Username</label>
					<input
						className="w-full rounded-lg border border-gray-300 p-3 focus:ring-2 focus:ring-indigo-500 focus:outline-none"
						placeholder="Choose a username"
						value={username}
						onChange={e => setUsername(e.target.value)}
						required
					/>
				</div>
				<div className="mb-6">
					<label className="mb-2 block font-semibold text-gray-700">Password</label>
					<input
						className="w-full rounded-lg border border-gray-300 p-3 focus:ring-2 focus:ring-indigo-500 focus:outline-none"
						type="password"
						placeholder="Choose a password"
						value={password}
						onChange={e => setPassword(e.target.value)}
						required
					/>
				</div>
				<button
					className="w-full rounded-lg bg-emerald-700 px-4 py-3 font-semibold text-white transition-colors hover:bg-emerald-800"
					type="submit"
				>
					Register
				</button>
			</form>
		</div>
	)
}
