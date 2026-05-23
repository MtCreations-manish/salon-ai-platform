import React from 'react'

const Button = ({ children, onClick, className = '', type = 'button', disabled }) => {
  return (
    <button
      type={type}
      onClick={onClick}
      disabled={disabled}
      className={`px-4 py-2 rounded-md bg-indigo-600 hover:bg-indigo-500 text-white font-medium ${className}`}
    >
      {children}
    </button>
  )
}

export default Button
