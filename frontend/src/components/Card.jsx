import React from 'react'

const Card = ({ title, children, className = '' }) => {
  return (
    <div className={`glass-card shadow-sm ${className}`}>
      {title && <div className="text-sm text-slate-300 mb-2">{title}</div>}
      <div className="text-lg font-medium">{children}</div>
    </div>
  )
}

export default Card
