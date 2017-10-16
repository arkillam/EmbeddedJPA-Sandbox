package ca.killam.EmbeddedDerbyJPAExample;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import ca.killam.EmbeddedDerbyJPAExample.entities.Person;

/**
 * 
 * Code based on my reading of http://www.vogella.com/tutorials/JavaPersistenceAPI/article.html#eclipselink .
 * 
 * @author Andrew Killam
 * @version 1.0 - 2017-09-17
 */

public class EmbeddedDerbyJPAExample {

	private static final String PERSISTENCE_UNIT_NAME = "people";

	public static void main(String[] args) {

		System.out.println("Started.");

		// by setting this, the relative path in persistence.xml will start from the user's home directory
		// e.g. on my computer, the final path used is "C:\Users\Andrew\databases\EmbeddedDerbyJPAExampleDB"
		System.setProperty("derby.system.home", System.getProperty("user.home"));

		// create the entity manager
		EntityManager em = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME).createEntityManager();

		// if there are no people in the database, create one
		List<Person> people = em.createQuery("select p from Person p where p.name = 'Andrew'", Person.class)
				.getResultList();
		if ((people == null) || (people.size() == 0)) {
			// no container to fall back on; have to create our on transaction
			em.getTransaction().begin();
			Person person = new Person();
			person.setName("Andrew");
			em.persist(person);
			em.getTransaction().commit();
			System.out.println("added " + person.getName());
		}

		// query the list of people in the database
		List<Person> check = em.createQuery("select p from Person p order by p.id", Person.class).getResultList();
		for (Person p : check) {
			System.out.println(String.format("Found:  %s (%d)", p.getName(), p.getId()));
		}

		System.out.println("Done.  Exiting.");
		System.exit(0);

	}

}
