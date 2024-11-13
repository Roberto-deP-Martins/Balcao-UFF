const Login = () => {
    return (
      <div className="flex flex-col items-center justify-center border border-gray-300 p-8 rounded-lg shadow-lg w-96 mx-auto mt-24 bg-white">
        <h2 className="text-2xl font-semibold mb-6">Entrar</h2>
        <input 
          type="text" 
          placeholder="CPF" 
          className="w-full p-3 mb-4 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
        <input 
          type="password" 
          placeholder="Senha" 
          className="w-full p-3 mb-4 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
        <button 
          className="w-full p-3 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition"
        >
          Logar
        </button>
        <a 
          href="#" 
          className="mt-4 text-sm text-blue-600 hover:underline"
        >
          Não tem conta? Faça seu cadastro aqui
        </a>
      </div>
    );
  };
  
  export default Login;
  