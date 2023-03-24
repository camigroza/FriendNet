package com.example.guiex1.services;

import com.example.guiex1.domain.Prietenie;
import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.repository.Repository;
import com.example.guiex1.utils.events.ChangeEventType;
import com.example.guiex1.utils.events.UtilizatorEntityChangeEvent;
import com.example.guiex1.utils.observer.Observable;
import com.example.guiex1.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UtilizatorService implements Observable<UtilizatorEntityChangeEvent> {
    private Repository<Long, Utilizator> repo;
    private PrietenieService prietenieService;
    private List<Observer<UtilizatorEntityChangeEvent>> observers=new ArrayList<>();

    public UtilizatorService(Repository<Long, Utilizator> repo, PrietenieService prietenieService) {
        this.repo = repo;
        this.prietenieService = prietenieService;
    }

    public PrietenieService getPrietenieService() {
        return prietenieService;
    }

    public Utilizator addUtilizator(Utilizator user) {
        if(repo.save(user).isEmpty()){
            UtilizatorEntityChangeEvent event = new UtilizatorEntityChangeEvent(ChangeEventType.ADD, user);
            notifyObservers(event);
            return null;
        }
        return user;
    }

    public Utilizator deleteUtilizator(Long id){
        Optional<Utilizator> user=repo.delete(id);
        if (user.isPresent()) {
            notifyObservers(new UtilizatorEntityChangeEvent(ChangeEventType.DELETE, user.get()));
            return user.get();}
        return null;
    }

    public Iterable<Utilizator> getAll(){
        return repo.findAll();
    }

    public Iterable<Utilizator> getFriends(Long idUser)
    {
        List<Utilizator> friends = new ArrayList<Utilizator>();
        int n = 0;
        List<Long> friendshipsIds = StreamSupport.stream(prietenieService.getIdsOfFriendshipsForUser(idUser).
                spliterator(), false).collect(Collectors.toList());
        for(Long id: friendshipsIds)
        {
            Prietenie p = prietenieService.findOne(id);
            if(Objects.equals(p.getStatus(), "accepted")) {
                Long idFriend = Long.valueOf(0);
                if (Objects.equals(p.getIdPrieten1(), idUser))
                    idFriend = p.getIdPrieten2();
                if (Objects.equals(p.getIdPrieten2(), idUser))
                    idFriend = p.getIdPrieten1();
                Utilizator u = repo.findOne(idFriend);
                if (u != null) {
                    friends.add(n++, u);
                }
            }
        }
        return friends;
    }

    public Iterable<Utilizator> getFriendsWithPendingToo(Long idUser)
    {
        List<Utilizator> friends = new ArrayList<Utilizator>();
        int n = 0;
        List<Long> friendshipsIds = StreamSupport.stream(prietenieService.getIdsOfFriendshipsForUser(idUser).
                spliterator(), false).collect(Collectors.toList());
        for(Long id: friendshipsIds)
        {
            Prietenie p = prietenieService.findOne(id);
            Long idFriend = Long.valueOf(0);
            if (Objects.equals(p.getIdPrieten1(), idUser))
                idFriend = p.getIdPrieten2();
            if (Objects.equals(p.getIdPrieten2(), idUser))
                idFriend = p.getIdPrieten1();
            Utilizator u = repo.findOne(idFriend);
            if (u != null) {
                friends.add(n++, u);
            }
        }
        return friends;
    }

    public Iterable<Utilizator> getFriendRequests(Long idUser)
    {
        List<Utilizator> friendRequests = new ArrayList<Utilizator>();
        int n = 0;
        List<Long> friendshipsIds = StreamSupport.stream(prietenieService.getIdsOfFriendshipsForUser(idUser).
                spliterator(), false).collect(Collectors.toList());
        for(Long id: friendshipsIds)
        {
            Prietenie p = prietenieService.findOne(id);
            Long idFriend = Long.valueOf(0);
            if(Objects.equals(p.getStatus(), "pending") && Objects.equals(p.getIdPrieten2(), idUser))
            {
                idFriend = p.getIdPrieten1();
                Utilizator u = repo.findOne(idFriend);
                friendRequests.add(n++, u);
            }
        }
        return friendRequests;
    }

    public Long getIdOfFriendshipOfTwoUsers(Long id1, Long id2)
    {
        List<Long> friendshipsIds = StreamSupport.stream(prietenieService.getIdsOfFriendshipsForUser(id1).
                spliterator(), false).collect(Collectors.toList());
        for(Long id: friendshipsIds)
        {
            Prietenie p = prietenieService.findOne(id);
            Long idFriend = 0L;
            if(Objects.equals(p.getIdPrieten1(), id1))
                idFriend = p.getIdPrieten2();
            if(Objects.equals(p.getIdPrieten2(), id1))
                idFriend = p.getIdPrieten1();
            if(Objects.equals(idFriend, id2))
                return id;
        }
        return null;
    }

    public Iterable<Utilizator> getPossibleFriends(Long idUser)
    {
        List<Utilizator> possibleFriends = new ArrayList<Utilizator>();
        int n = 0;
        Iterable<Utilizator> users = this.getFriendsWithPendingToo(idUser);
        List<Utilizator> friends = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());
        List<Utilizator> allUsers = StreamSupport.stream(this.getAll().spliterator(), false)
                .collect(Collectors.toList());
        for(Utilizator u: allUsers)
        {
            if(!friends.contains(u) && !Objects.equals(u.getId(), idUser))
            {
                possibleFriends.add(n++, u);
            }
        }
        return possibleFriends;
    }

    @Override
    public void addObserver(Observer<UtilizatorEntityChangeEvent> e) {
        observers.add(e);

    }

    @Override
    public void removeObserver(Observer<UtilizatorEntityChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(UtilizatorEntityChangeEvent t) {

        observers.stream().forEach(x->x.update(t));
    }

}
