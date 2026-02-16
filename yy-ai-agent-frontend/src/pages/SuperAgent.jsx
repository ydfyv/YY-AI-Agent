import { useState, useEffect, useRef } from 'react'
import { useNavigate } from 'react-router-dom'
import { API } from '../utils/api'
import { RobotIcon, RocketIcon, UserIcon, BackIcon } from '../components/Icons'
import Footer from '../components/Footer'
import './ChatRoom.css'

function SuperAgent() {
  const navigate = useNavigate()
  const [messages, setMessages] = useState([])
  const [input, setInput] = useState('')
  const [isLoading, setIsLoading] = useState(false)
  const [isStreaming, setIsStreaming] = useState(false)
  const messagesEndRef = useRef(null)
  const eventSourceRef = useRef(null)

  useEffect(() => {
    scrollToBottom()
  }, [messages])

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' })
  }

  const handleSendMessage = async () => {
    if (!input.trim() || isLoading) return

    const userMessage = input.trim()
    setInput('')
    setMessages(prev => [...prev, { role: 'user', content: userMessage }])
    setIsLoading(true)
    setIsStreaming(true)

    try {
      const url = `${API.YYMANUS_SSE}?prompt=${encodeURIComponent(userMessage)}`
      eventSourceRef.current = new EventSource(url)

      let aiMessage = { role: 'assistant', content: '' }
      setMessages(prev => [...prev, aiMessage])

      eventSourceRef.current.onmessage = (event) => {
        const chunk = event.data
        setMessages(prev => {
          const newMessages = [...prev]
          const lastMessage = newMessages[newMessages.length - 1]
          if (lastMessage.role === 'assistant') {
            lastMessage.content += chunk
          }
          return newMessages
        })
      }

      eventSourceRef.current.onerror = (error) => {
        console.error('SSE error:', error)
        eventSourceRef.current.close()
        setIsLoading(false)
        setIsStreaming(false)
      }

      eventSourceRef.current.addEventListener('close', () => {
        eventSourceRef.current.close()
        setIsLoading(false)
        setIsStreaming(false)
      })

    } catch (error) {
      console.error('Error sending message:', error)
      setIsLoading(false)
      setIsStreaming(false)
    }
  }

  const handleKeyPress = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault()
      handleSendMessage()
    }
  }

  const handleBackToHome = () => {
    if (eventSourceRef.current) {
      eventSourceRef.current.close()
    }
    navigate('/')
  }

  return (
    <div className="chat-room">
      <div className="chat-header">
        <button className="back-button" onClick={handleBackToHome}>
          <BackIcon className="back-icon" />
          返回
        </button>
        <div className="chat-title">
          <span className="chat-icon"><RobotIcon /></span>
          AI 超级智能体
        </div>
        {isStreaming && <div className="header-loading-indicator">
          <div className="loading-dot"></div>
          <div className="loading-dot"></div>
          <div className="loading-dot"></div>
        </div>}
      </div>

      <div className="chat-messages">
        {messages.length === 0 && (
          <div className="welcome-message">
            <div className="welcome-icon"><RocketIcon /></div>
            <h2>欢迎使用 AI 超级智能体</h2>
            <p>我是全能型AI助手，可以处理各类复杂任务和问题</p>
          </div>
        )}
        {messages.map((message, index) => (
          <div key={index} className={`message ${message.role}`}>
            <div className="message-avatar">
              {message.role === 'user' ? <UserIcon /> : <RobotIcon />}
            </div>
            <div className="message-content">
              {message.role === 'assistant' && index === messages.length - 1 && isLoading && (
                <div className="typing-indicator">
                  <span></span>
                  <span></span>
                  <span></span>
                </div>
              )}
              <div className="message-text">{message.content}</div>
            </div>
          </div>
        ))}
        <div ref={messagesEndRef} />
      </div>

      <div className="chat-input-container">
        {isLoading && (
          <div className="global-loading">
            <div className="loading-spinner"></div>
            <span>AI 正在思考中...</span>
          </div>
        )}
        <div className="chat-input-wrapper">
          <textarea
            className="chat-input"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyPress={handleKeyPress}
            placeholder="输入您的问题..."
            disabled={isLoading}
            rows={1}
          />
          <button 
            className={`send-button ${isLoading ? 'disabled' : ''}`}
            onClick={handleSendMessage}
            disabled={isLoading}
          >
            {isLoading ? '发送中...' : '发送'}
          </button>
        </div>
      </div>
      <Footer />
    </div>
  )
}

export default SuperAgent
