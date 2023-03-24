package com.example.guiex1.services;

import com.example.guiex1.domain.Prietenie;
import com.example.guiex1.repository.Repository;
import com.example.guiex1.utils.events.ChangeEventType;
import com.example.guiex1.utils.events.PrietenieEntityChangeEvent;
import com.example.guiex1.utils.observer.Observable;
import com.example.guiex1.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PrietenieService implements Observable<PrietenieEntityChangeEvent> {
    private Repository<Long, Prietenie> repo;
    private List<Observer<PrietenieEntityChangeEvent>> observers=new ArrayList<>();

    public PrietenieService(Repository<Long, Prietenie> repo) {
        this.repo = repo;
    }


    public Repository<Long, Prietenie> getRepo() {
        return repo;
    }

    public Prietenie addPrietenie(Prietenie friendship) {
        if(repo.save(friendship).isEmpty()){
            PrietenieEntityChangeEvent event = new PrietenieEntityChangeEvent(ChangeEventType.ADD, friendship);
            notifyObservers(event);
            return null;
        }
        return friendship;
    }

    public Prietenie deletePrietenie(Long id){
        Optional<Prietenie> user=repo.delete(id);
        if (user.isPresent()) {
            notifyObservers(new PrietenieEntityChangeEvent(ChangeEventType.DELETE, user.get()));
            return user.get();}
        return null;
    }

    public Iterable<Prietenie> getAll(){
        return repo.findAll();
    }

    public Iterable<Long> getIdsOfFriendshipsForUser(Long idUser)
    {
        Iterable<Prietenie> allFriendships = repo.findAll();
        List<Prietenie> friendshipsNeeded = StreamSupport.stream(allFriendships.spliterator(), false)
                .filter(x-> (Objects.equals(x.getIdPrieten1(), idUser) ||
                        Objects.equals(x.getIdPrieten2(), idUser)))
                .collect(Collectors.toList());
        List<Long> ids = new ArrayList<Long>();
        int n = 0;
        for(Prietenie p: friendshipsNeeded)
        {
            ids.add(n++, p.getId());
        }
        return ids;
    }

    public Prietenie findOne(Long id)
    {
        return repo.findOne(id);
    }

    public LocalDateTime findDateTime(Long id)
    {
        return repo.findOne(id).getDate();
    }

    @Override
    public void addObserver(Observer<PrietenieEntityChangeEvent> e) {
        observers.add(e);

    }

    @Override
    public void removeObserver(Observer<PrietenieEntityChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(PrietenieEntityChangeEvent t) {

        observers.stream().forEach(x->x.update(t));
    }
}
