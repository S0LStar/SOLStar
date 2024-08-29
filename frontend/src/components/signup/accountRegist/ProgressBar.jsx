import React from 'react';
import './ProgressBar.css';

function ProgressBar({ currentStep }) {
  return (
    <div className="progress-bar">
      {[1, 2, 3, 4].map((step) => (
        <div
          key={step}
          className={`progress-step ${currentStep === step ? 'active' : ''} ${
            currentStep > step ? 'completed' : ''
          } ${currentStep < step ? 'upcoming' : ''}`}
        >
          <div className="circle">{currentStep === step ? step : ''}</div>
        </div>
      ))}
    </div>
  );
}

export default ProgressBar;
