for env in *.env; do
  source "$env" 
done

sudo docker start frugal-fennec-expense-manager_db_1 && java -jar `ls ./target/frugal-fennec-expense-manager-*.jar`
