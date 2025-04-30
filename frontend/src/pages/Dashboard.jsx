import React from 'react';
import { useAuthRedirect } from '../hooks/useAuthRedirect';
import AdminDashboard from '../components/dashboard/AdminDashboard';
import VenueManagerDashboard from '../components/dashboard/VenueManagerDashboard';
import PlayerDashboard from '../components/dashboard/PlayerDashboard';
import Analytics from '../components/dashboard/Analytics';

const Dashboard = () => {
  const { user, isAuthenticated } = useAuthRedirect();

  if (!isAuthenticated) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  if (!user) return null;

  const renderDashboard = () => {
    switch (user.role) {
      case 'ADMIN':
        return (
          <div className="space-y-8">
            <AdminDashboard />
            <Analytics />
          </div>
        );
      case 'VENUE_MANAGER':
        return <VenueManagerDashboard />;
      case 'PLAYER':
        return <PlayerDashboard />;
      default:
        return <div>No dashboard available for this role</div>;
    }
  };

  return (
    <div className="max-w-7xl mx-auto">
      <h1 className="text-3xl font-bold text-gray-900 mb-8">Welcome back, {user.username}!</h1>
      {renderDashboard()}
    </div>
  );
};

export default Dashboard; 