const Home = () => {
  return (
    <div className="relative bg-cover bg-center h-screen" style={{ backgroundImage: 'url("https://source.unsplash.com/random/1600x900")' }}>
      <div className="absolute inset-0 bg-black opacity-50"></div>
      <div className="relative z-10 flex flex-col items-center justify-center h-full text-center text-white px-4">
        <h1 className="text-5xl font-bold mb-6">Welcome to the Future of Learning</h1>
        <p className="text-lg mb-8">Explore our courses, tutorials, and content to grow your skills</p>
        <button className="px-6 py-3 text-xl font-semibold text-white bg-blue-500 hover:bg-blue-600 rounded-lg transition-colors">
          Start Your Journey
        </button>
      </div>
    </div>
  );
};

export default Home;
