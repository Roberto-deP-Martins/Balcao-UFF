const Home = () => {
  return (
    <div className="relative bg-cover bg-center" style={{ backgroundImage: 'url("https://source.unsplash.com/random/1600x900")' }}>
      <div className="relative z-10 flex flex-col items-center justify-center h-full text-center text-black px-4">
        <h1 className="text-5xl font-bold mb-6">Welcome to the system</h1>
        <p className="text-lg mb-8">Explore advertises and more</p>
        <button className="px-6 py-3 text-xl font-semibold text-black bg-blue-500 hover:bg-blue-600 rounded-lg transition-colors">
          Start Your Journey
        </button>
      </div>
    </div>
  );
};

export default Home;
