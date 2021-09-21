CREATE TABLE cliente(
	id integer PRIMARY KEY AUTO_INCREMENT,
	cpf varchar(11),
	nome VARCHAR(100)
);

CREATE TABLE produto(
	id integer PRIMARY KEY AUTO_INCREMENT,
	descricao varchar(100),
	preco_unitario numeric(20,2)
);

CREATE TABLE pedido(
	id integer PRIMARY KEY AUTO_INCREMENT,
	id_cliente integer REFERENCES cliente(id),
	data_pedido timestamp,
	status varchar(20),
	total numeric(20,2)
);

CREATE TABLE item_pedido(
	id integer PRIMARY KEY AUTO_INCREMENT,
	id_produto integer REFERENCES produto(id),
	id_pedido integer REFERENCES pedido(id),
	quantidade integer
);