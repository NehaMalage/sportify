import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import api from '../api/api';
import { toast } from 'react-toastify';

const PlayerBookings = () => {
  const { user } = useAuth();
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchBookings = async () => {
      try {
        console.log('Fetching bookings for user:', user);
        const response = await api.get(`/bookings/user/id/${user.id}`);
        console.log('Bookings response:', response.data);
        setBookings(response.data);
      } catch (error) {
        console.error('Error fetching bookings:', error);
        console.error('Error details:', error.response?.data);
        toast.error('Failed to load bookings');
      } finally {
        setLoading(false);
      }
    };

    if (user) {
      fetchBookings();
    }
  }, [user]);

  if (loading) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold text-gray-800 mb-6">My Bookings</h1>
      
      {bookings.length === 0 ? (
        <div className="text-center py-8">
          <p className="text-gray-600">You haven't made any bookings yet.</p>
        </div>
      ) : (
        <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
          {bookings.map((booking) => (
            <div key={booking.id} className="bg-white rounded-lg shadow-md p-6">
              <div className="flex justify-between items-start mb-4">
                <div>
                  <h3 className="text-lg font-semibold text-gray-800">{booking.venue.name}</h3>
                  <p className="text-sm text-gray-600">Court {booking.courtNumber}</p>
                </div>
                <span className={`px-3 py-1 rounded-full text-sm font-medium ${
                  booking.status === 'CONFIRMED' ? 'bg-green-100 text-green-800' :
                  booking.status === 'PENDING' ? 'bg-yellow-100 text-yellow-800' :
                  'bg-red-100 text-red-800'
                }`}>
                  {booking.status}
                </span>
              </div>
              
              <div className="space-y-2">
                <div className="flex justify-between text-sm">
                  <span className="text-gray-600">Date:</span>
                  <span className="font-medium">{new Date(booking.bookingDate).toLocaleDateString()}</span>
                </div>
                <div className="flex justify-between text-sm">
                  <span className="text-gray-600">Time:</span>
                  <span className="font-medium">{booking.startTime} - {booking.endTime}</span>
                </div>
                <div className="flex justify-between text-sm pt-2 border-t mt-2">
                  <span className="text-gray-600">Amount:</span>
                  <span className="font-medium">₹{booking.totalAmount}</span>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default PlayerBookings;
