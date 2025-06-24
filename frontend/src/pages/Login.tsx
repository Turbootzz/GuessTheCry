import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { login } from '../utils/api'
import { useAuth } from '../context/AuthContext'

export default function Login() {
	const [username, setUsername] = useState('')
	const [password, setPassword] = useState('')
	const { login: setAuth } = useAuth()
	const navigate = useNavigate()

	const handleSubmit = async (e: React.FormEvent) => {
		e.preventDefault()
		try {
			const { user, token } = await login(username, password)
			setAuth(user, token)
			navigate('/profile')
		} catch {
			alert('Login failed')
		}
	}

	return (
		<form
			onSubmit={handleSubmit}
			className="mx-auto mt-20 max-w-md rounded bg-white p-4 shadow"
		>
			<h2 className="mb-4 text-xl font-bold">Login</h2>
			<input
				className="mb-2 w-full border p-2"
				placeholder="Username"
				value={username}
				onChange={e => setUsername(e.target.value)}
			/>
			<input
				className="mb-4 w-full border p-2"
				type="password"
				placeholder="Password"
				value={password}
				onChange={e => setPassword(e.target.value)}
			/>
			<button className="w-full rounded bg-emerald-700 px-4 py-2 text-white" type="submit">
				Login
			</button>
		</form>
	)
}
