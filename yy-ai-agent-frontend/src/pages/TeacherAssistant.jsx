import { useState, useEffect, useRef } from 'react'
import { useNavigate } from 'react-router-dom'
import { API, generateChatId } from '../utils/api'
import { TeacherIcon, WelcomeIcon, UserIcon, RobotIcon, BackIcon } from '../components/Icons'
import Footer from '../components/Footer'
import './ChatRoom.css'

function TeacherAssistant() {
  const navigate = useNavigate()
  const [messages, setMessages] = useState([])
  const [input, setInput] = useState('')
  const [isLoading, setIsLoading] = useState(false)
  const [isStreaming, setIsStreaming] = useState(false)
  const [chatId] = useState(() => generateChatId())
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
    setMessages(prev => [...prev, { role: 'user', content: userMessage }, { role: 'assistant', content: '' }])
    setIsLoading(true)
    setIsStreaming(true)

    try {
      const url = `${API.TEACHER_ASSISTANT_SSE}?prompt=${encodeURIComponent(userMessage)}&chatId=${chatId}`
      eventSourceRef.current = new EventSource(url)

      eventSourceRef.current.onmessage = (event) => {
        try {
          const chunk = event.data
          if (chunk) {
            setMessages(prev => {
              const newMessages = [...prev]
              const lastMessage = newMessages[newMessages.length - 1]
              if (lastMessage && lastMessage.role === 'assistant') {
                return [
                  ...newMessages.slice(0, -1),
                  { ...lastMessage, content: lastMessage.content + chunk }
                ]
              }
              return newMessages
            })
          }
        } catch (error) {
          console.error('Error processing SSE message:', error)
        }
      }

      eventSourceRef.current.onerror = (error) => {
        console.error('SSE connection error:', {
          type: error.type,
          target: error.target,
          readyState: error.target?.readyState
        })
        eventSourceRef.current?.close()
        setIsLoading(false)
        setIsStreaming(false)
        setMessages(prev => {
          const newMessages = [...prev]
          const lastMessage = newMessages[newMessages.length - 1]
          if (lastMessage && lastMessage.role === 'assistant' && !lastMessage.content) {
            return [
              ...newMessages.slice(0, -1),
              { ...lastMessage, content: '连接失败，请检查后端服务是否正常运行' }
            ]
          }
          return newMessages
        })
      }

      eventSourceRef.current.addEventListener('close', () => {
        eventSourceRef.current?.close()
        setIsLoading(false)
        setIsStreaming(false)
      })

      eventSourceRef.current.addEventListener('end', () => {
        eventSourceRef.current?.close()
        setIsLoading(false)
        setIsStreaming(false)
      })

    } catch (error) {
      console.error('Error sending message:', error)
      setIsLoading(false)
      setIsStreaming(false)
      setMessages(prev => {
        const newMessages = [...prev]
        const lastMessage = newMessages[newMessages.length - 1]
        if (lastMessage && lastMessage.role === 'assistant' && !lastMessage.content) {
          return [
            ...newMessages.slice(0, -1),
            { ...lastMessage, content: `发送失败: ${error.message || '未知错误'}` }
          ]
        }
        return newMessages
      })
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
        <div className="chat-header-center">
          <div className="chat-title">
            <span className="chat-icon"><TeacherIcon /></span>
            AI 教师助手
          </div>
          <div className="chat-room-id">
            <span className="room-id-label">房间号：</span>
            <span className="room-id-value">{chatId}</span>
            <button 
              className="copy-button" 
              onClick={() => navigator.clipboard.writeText(chatId)}
              title="复制房间号"
            >
              📋
            </button>
          </div>
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
            <div className="welcome-icon"><WelcomeIcon /></div>
            <h2>欢迎使用 AI 教师助手</h2>
            <p>我是您的教学辅助工具，有什么可以帮助您的吗？</p>
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

export default TeacherAssistant
