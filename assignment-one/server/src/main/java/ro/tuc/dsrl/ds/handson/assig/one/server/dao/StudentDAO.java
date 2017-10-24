package ro.tuc.dsrl.ds.handson.assig.one.server.dao;

import ro.tuc.dsrl.ds.handson.assig.one.server.entities.Student;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.*;

import java.util.List;

/**
 * @Author: Technical University of Cluj-Napoca, Romania
 * Distributed Systems, http://dsrl.coned.utcluj.ro/
 * @Module: assignment-one-server
 * @Since: Sep 1, 2015
 * @Description: Uses Hibernate for CRUD operations on the underlying database.
 * The Hibernate configuration files can be found in the src/main/resources folder
 */
public class StudentDAO {
    private static final Log LOGGER = LogFactory.getLog(StudentDAO.class);

    private SessionFactory factory;

    public StudentDAO(SessionFactory factory) {
        this.factory = factory;
    }

    public Student addStudent(Student student) {
        int studentId = -1;
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            studentId = (Integer) session.save(student);
            student.setId(studentId);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            LOGGER.error("", e);
        } finally {
            session.close();
        }
        return student;
    }

    @SuppressWarnings("unchecked")
    public List<Student> findStudents() {
        Session session = factory.openSession();
        Transaction tx = null;
        List<Student> students = null;
        try {
            tx = session.beginTransaction();
            students = session.createQuery("FROM Student").list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            LOGGER.error("", e);
        } finally {
            session.close();
        }
        return students;
    }

    @SuppressWarnings("unchecked")
    public Student findStudent(int id) {
        Session session = factory.openSession();
        Transaction tx = null;
        List<Student> students = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("FROM Student WHERE id = :id");
            query.setParameter("id", id);
            students = query.list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            LOGGER.error("", e);
        } finally {
            session.close();
        }
        return students != null && !students.isEmpty() ? students.get(0) : null;
    }

    @SuppressWarnings("unchecked")
    public Student deleteStudent(int studentId) {
        Session session = factory.openSession();
        Transaction tx = null;
        List<Student> students;
        Student deletedStudent = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("FROM Student WHERE id = :id");
            query.setParameter("id", studentId);
            students = query.list();

            if (students != null && !students.isEmpty()) {
                deletedStudent = students.get(0);
                session.delete(deletedStudent);
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            LOGGER.error("", e);
        } finally {
            session.close();
        }
        return deletedStudent;
    }
}
