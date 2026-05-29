import RootRouter from "./router/RootRouter";
import { AuthProvider } from "./auth/AuthContext";
import './App.css';

function App() {
  return (
    <AuthProvider>
      <RootRouter />
    </AuthProvider>
  );
}

export default App;
