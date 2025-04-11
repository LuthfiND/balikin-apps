package org.balikin.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.balikin.entity.Inbox;

@ApplicationScoped
public class InboxRepository  implements PanacheRepositoryBase<Inbox,Integer> {

}
