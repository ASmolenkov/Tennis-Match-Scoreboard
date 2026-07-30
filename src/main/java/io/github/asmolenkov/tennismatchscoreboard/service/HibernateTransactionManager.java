package io.github.asmolenkov.tennismatchscoreboard.service;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.function.Supplier;
@AllArgsConstructor
public class HibernateTransactionManager implements TransactionManager {
    private final SessionFactory sessionFactory;
    @Override
    public <T> T executeInTransaction(Supplier<T> action) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        try {
            T result = action.get();
            transaction.commit();
            return result;
        }catch (Exception e){
            if(transaction.isActive()){
                transaction.rollback();
            }
            throw e;
        }
    }
}
