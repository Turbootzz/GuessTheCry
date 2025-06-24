import { Link } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'

export default function Navbar() {
	const { user, logout } = useAuth()

	return (
		<nav className="flex items-center justify-between bg-white p-4 shadow">
			<Link to="/" className="text-lg font-bold text-indigo-600">
				GuessTheCry
			</Link>
			<div>
				{user ? (
					<>
						<Link to="/profile" className="mr-4 text-indigo-700">
							{user.username}
						</Link>
						<button onClick={logout} className="text-red-500">
							Logout
						</button>
					</>
				) : (
					<>
						<Link to="/login" className="mr-4">
							Login
						</Link>
						<Link to="/register">Register</Link>
					</>
				)}
			</div>
		</nav>
	)
}
