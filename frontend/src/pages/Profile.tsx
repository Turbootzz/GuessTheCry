import { useEffect, useState } from 'react'
import { useAuth } from '../context/AuthContext'
import { fetchUserStats } from '../utils/api'

// stats from the backend
interface UserStats {
	difficulty: string
	gamesPlayed: number
	averageAccuracy: number
}

export default function Profile() {
	const auth = useAuth()
	const { user } = auth
	const [stats, setStats] = useState<UserStats[] | null>(null)
	const [error, setError] = useState<string | null>(null)

	useEffect(() => {
		if (!user?.id) return

		const controller = new AbortController()

		fetchUserStats(user.id, auth)
			.then(data => {
				if (!controller.signal.aborted) setStats(data)
			})
			.catch(err => {
				if (!controller.signal.aborted) {
					setError(err instanceof Error ? err.message : 'Failed to load profile data.')
				}
			})

		return () => controller.abort()
	}, [user, auth])

	if (!user) {
		return <div className="text-center text-xl text-red-500">User not found.</div>
	}

	if (error) {
		return <div className="text-center text-xl text-red-500">{error}</div>
	}

	if (stats === null) {
		return <div className="text-center text-xl">Loading profile...</div>
	}

	return (
		<div className="w-full max-w-2xl rounded-lg bg-white p-8 shadow-xl">
			<h1 className="mb-2 text-4xl font-bold text-gray-800">
				Welcome, <span className="text-emerald-700">{user?.username}</span>!
			</h1>
			<p className="mb-8 text-lg text-gray-600">Here are your game statistics.</p>

			{stats.length > 0 ? (
				<div className="space-y-6">
					{stats.map(stat => (
						<div
							key={stat.difficulty}
							className="rounded-md border border-gray-200 bg-gray-50 p-6"
						>
							<h2 className="mb-3 text-2xl font-semibold text-gray-700 capitalize">
								{stat.difficulty} Mode
							</h2>
							<div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
								<div className="text-center">
									<p className="text-4xl font-bold text-emerald-700">
										{stat.gamesPlayed}
									</p>
									<p className="text-sm font-medium text-gray-500">
										Games Played
									</p>
								</div>
								<div className="text-center">
									<p className="text-4xl font-bold text-emerald-700">
										{stat.averageAccuracy.toFixed(1)}%
									</p>
									<p className="text-sm font-medium text-gray-500">
										Average Accuracy
									</p>
								</div>
							</div>
						</div>
					))}
				</div>
			) : (
				<p className="mt-6 rounded-md bg-blue-50 p-4 text-center text-gray-700">
					You haven't played any games yet.{' '}
					<a href="/" className="font-bold text-emerald-700 hover:underline">
						Start a new game
					</a>{' '}
					to see your stats here!
				</p>
			)}
		</div>
	)
}
