import React from 'react'

const LoadingSpinner = ({ size = 6 }) => {
  return (
    <div className={`animate-spin rounded-full border-4 border-t-transparent border-white/30 w-${size} h-${size}`}></div>
  )
}

export default LoadingSpinner
