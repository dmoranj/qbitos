(defproject qbitos "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
                 [incanter "1.5.6"]
                 [org.clojure/clojure "1.6.0"]]
  :main ^:skip-aot qbitos.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
