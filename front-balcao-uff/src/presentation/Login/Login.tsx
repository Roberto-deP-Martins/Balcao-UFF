import { useState } from 'react';

const Login = () => {
  const [cpf, setCpf] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    console.log('CPF:', cpf);
    console.log('Password:', password);
  };

  const handleCpfChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setCpf(e.target.value);
  };

  const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(e.target.value);
  };

  return (
    <div className="flex flex-col items-center justify-center border border-gray-300 p-8 rounded-lg shadow-lg w-96 mx-auto mt-24 bg-white">
      <h2 className="text-2xl font-semibold mb-6">Entrar</h2>
      <form onSubmit={(e) => handleSubmit(e)}>
        <input 
          type="text" 
          placeholder="CPF" 
          value={cpf}
          onChange={handleCpfChange}
          className="w-full p-3 mb-4 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
        <input 
          type="password" 
          placeholder="Senha" 
          value={password}
          onChange={handlePasswordChange}
          className="w-full p-3 mb-4 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
        <button
          type='submit' 
          className="w-full p-3 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition"
        >
          Logar
        </button>
      </form>
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
