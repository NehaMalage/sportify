const handleAddVenue = async (e) => {
    e.preventDefault();
    try {
        const response = await axios.post('/api/venues', newVenue, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });
        
        if (response.data) {
            setVenues([...venues, response.data]);
            setNewVenue({
                name: '',
                location: '',
                totalCourts: '',
                pricePerHour: '',
                description: ''
            });
            toast.success('Venue added successfully!');
        } else {
            throw new Error('Failed to add venue: No data received');
        }
    } catch (error) {
        console.error('Error adding venue:', error);
        const errorMessage = error.response?.data?.message || error.message || 'Failed to add venue';
        toast.error(errorMessage);
    }
}; 