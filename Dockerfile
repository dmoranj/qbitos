FROM clojure
WORKDIR /opt

RUN wget https://github.com/dmoranj/qbitos/archive/master.zip
RUN unzip master.zip

WORKDIR /opt/qbitos-master
RUN lein deps && lein compile && lein install

ENTRYPOINT lein repl

