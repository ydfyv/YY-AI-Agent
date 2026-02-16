import { GithubIcon } from './Icons'
import './Footer.css'

function Footer() {
  return (
    <footer className="app-footer">
      <div className="footer-content">
        <div className="footer-copyright">
          Powered by 阿狸
        </div>
        <div className="footer-links">
          <a 
            href="https://github.com/ydfyv" 
            target="_blank" 
            rel="noopener noreferrer"
            className="footer-link"
          >
            程序员阿狸
          </a>
          <a 
            href="https://github.com/ydfyv" 
            target="_blank" 
            rel="noopener noreferrer"
            className="footer-link footer-icon-link"
            title="GitHub"
          >
            <GithubIcon className="github-icon" />
          </a>
        </div>
      </div>
    </footer>
  )
}

export default Footer
