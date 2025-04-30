import React, { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

const DashboardLayout = ({ children }) => {
  const [isSidebarOpen, setIsSidebarOpen] = useState(true);
  const { userRole, username, logout, isLoading } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  // Add loading state check
  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  // Redirect if not authenticated
  if (!username) {
    navigate('/login');
    return null;
  }

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const getRoleBasedNavItems = () => {
    const commonItems = [
      { title: 'Dashboard', path: '/dashboard', icon: '🏠' },
      { title: 'Profile', path: `/profile/${username}`, icon: '👤' },
    ];

    const adminItems = [
      { title: 'User Management', path: '/admin/users', icon: '👥' },
      { title: 'Venue Management', path: '/admin/venues', icon: '🏟️' },
      { title: 'Booking Management', path: '/admin/bookings', icon: '📅' },
    ];

    const venueManagerItems = [
      { title: 'My Venues', path: '/manager/venues', icon: '🏟️' },
      { title: 'Bookings', path: '/manager/bookings', icon: '📅' },
      { title: 'Analytics', path: '/manager/analytics', icon: '📊' },
    ];

    const playerItems = [
      { title: 'Find Venues', path: '/venues', icon: '🔍' },
      { title: 'My Bookings', path: '/bookings', icon: '📅' }
    ];

    switch (userRole) {
      case 'ADMIN':
        return [...commonItems, ...adminItems];
      case 'VENUE_MANAGER':
        return [...commonItems, ...venueManagerItems];
      case 'PLAYER':
        return [...commonItems, ...playerItems];
      default:
        return commonItems;
    }
  };

  // Get role display name
  const getRoleDisplayName = () => {
    switch (userRole) {
      case 'ADMIN':
        return 'Administrator';
      case 'VENUE_MANAGER':
        return 'Venue Manager';
      case 'PLAYER':
        return 'Player';
      default:
        return userRole;
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Sidebar */}
      <div
        className={`fixed inset-y-0 left-0 transform ${
          isSidebarOpen ? 'translate-x-0' : '-translate-x-full'
        } md:translate-x-0 transition-all duration-300 ease-in-out bg-gradient-to-b from-gray-900 to-gray-800 w-64 z-30`}
      >
        <div className="flex flex-col h-full">
          {/* Logo Section */}
          <div className="flex items-center h-16 px-6 bg-gradient-to-r from-blue-600 to-blue-700">
            <Link to="/" className="flex items-center space-x-3">
              <span className="text-2xl">⚡</span>
              <span className="text-xl font-bold text-white">Sportify</span>
            </Link>
          </div>

          {/* User Profile Section */}
          <div className="mt-6 px-6">
            <div className="flex items-center space-x-4 bg-gray-800 rounded-lg p-3">
              <div className="flex items-center justify-center w-10 h-10 rounded-full bg-blue-500 text-white font-semibold">
                {username && username[0].toUpperCase()}
              </div>
              <div className="flex-1 min-w-0">
                <p className="text-sm font-medium text-white truncate">{username}</p>
                <p className="text-xs text-gray-400">{getRoleDisplayName()}</p>
              </div>
            </div>
          </div>

          {/* Navigation */}
          <nav className="flex-1 px-4 py-6 space-y-1 overflow-y-auto">
            {getRoleBasedNavItems().map((item) => (
              <Link
                key={item.path}
                to={item.path}
                className={`flex items-center px-4 py-3 text-sm font-medium rounded-lg transition-all duration-200 ${
                  location.pathname === item.path
                    ? 'bg-blue-500 text-white'
                    : 'text-gray-300 hover:bg-gray-700 hover:text-white'
                }`}
              >
                <span className="text-xl mr-3">{item.icon}</span>
                {item.title}
              </Link>
            ))}
          </nav>

          {/* Logout Button */}
          <div className="p-4">
            <button
              onClick={handleLogout}
              className="flex items-center w-full px-4 py-2 text-sm font-medium text-gray-300 rounded-lg hover:bg-gray-700 hover:text-white transition-colors duration-200"
            >
              <span className="text-xl mr-3">🚪</span>
              Logout
            </button>
          </div>
        </div>
      </div>

      {/* Main Content */}
      <div className={`${isSidebarOpen ? 'md:ml-64' : 'md:ml-0'} transition-all duration-300`}>
        {/* Top Bar */}
        <div className="sticky top-0 z-20 bg-white">
          <div className="flex items-center justify-between h-16 px-6 border-b border-gray-200">
            <div className="flex items-center">
              <button
                onClick={() => setIsSidebarOpen(!isSidebarOpen)}
                className="p-2 rounded-lg text-gray-600 hover:bg-gray-100 focus:outline-none focus:ring md:hidden"
              >
                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
                </svg>
              </button>
              
              {/* Page Title */}
              <h1 className="ml-4 text-xl font-semibold text-gray-800">Dashboard</h1>
            </div>

            {/* User Role Badge */}
            <div className="flex items-center space-x-4">
              <div className="px-4 py-2 rounded-full bg-blue-100 text-blue-700 font-medium text-sm">
                {getRoleDisplayName()}
              </div>
            </div>
          </div>
        </div>

        {/* Page Content */}
        <main className="p-6">{children}</main>
      </div>
    </div>
  );
};

export default DashboardLayout; 