import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import '../styles/Home.css'; // Make sure this CSS file exists

const Home = () => {
  const { isLoggedIn } = useAuth();
  const navigate = useNavigate();

  const handleExplore = () => {
    if (isLoggedIn) {
      navigate('/dashboard');
    } else {
      navigate('/login');
    }
  };

  return (
    <div className="home-container">
      {/* Hero Section */}
      <div className="hero-section">
        <div className="hero-content">
          <h1 className="hero-title">
            Welcome to <span className="text-gradient">Sportify</span>
          </h1>
          <p className="hero-subtitle">
            Where passion meets the perfect venue. Experience seamless sports facility booking.
          </p>
          <button onClick={handleExplore} className="explore-btn">
            {isLoggedIn ? 'Explore ' : 'Get Started'} 
            <span className="btn-icon">→</span>
          </button>
        </div>
        
        <div className="sports-icons">
          <div className="icon-row">🏸</div>
          <div className="icon-row">⚽️</div>
          <div className="icon-row">🏀</div>
          <div className="icon-row">🎾</div>
        </div>
      </div>

      {/* Features Section */}
      <div className="benefits-section">
        <div className="benefit-card">
          <span className="benefit-icon">⚡</span>
          <h3>Quick & Easy</h3>
          <p>Book your favorite sports venue in minutes</p>
        </div>
        <div className="benefit-card">
          <span className="benefit-icon">🌟</span>
          <h3>Premium Venues</h3>
          <p>Access to top-quality facilities</p>
        </div>
        <div className="benefit-card">
          <span className="benefit-icon">💪</span>
          <h3>Stay Active</h3>
          <p>Your gateway to an active lifestyle</p>
        </div>
      </div>
    </div>
  );
};

export default Home;
