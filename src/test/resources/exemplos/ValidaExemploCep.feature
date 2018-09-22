# language: pt
Funcionalidade: Validar API CEP

  Contexto: 
    Dado que acesse a api cep
      | api     | cep      |
      | API_CEP | 91180650 |
    E que gere a massa de dados para api cep
      | cep      | logradouro        | complemento | bairro        | localidade   | uf | unidade | ibge    | gia  |
      | 91180650 | Rua José Grimberg | Casa 1      | Costa e Silva | Porto Alegre | RS |    1956 | 4314902 | 1207 |

  Cenario: Validar API CEP
    Quando valido a existencia dos campos da api cep
    Entao valido a integridade dos dados dos campos da api cep
