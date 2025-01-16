import { Mensagem } from "../interfaces";

export interface CreateConversa {
  interessadoFecharNegocio: any;
  id: number;
  mensagens: Mensagem[];
}
