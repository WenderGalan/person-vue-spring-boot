package io.github.wendergalan.personapi.services.impl;

import io.github.wendergalan.personapi.controllers.PhysicalPersonController;
import io.github.wendergalan.personapi.helper.Helper;
import io.github.wendergalan.personapi.model.dtos.PhysicalPersonDTOV1;
import io.github.wendergalan.personapi.model.dtos.PhysicalPersonDTOV2;
import io.github.wendergalan.personapi.model.entities.PhysicalPerson;
import io.github.wendergalan.personapi.model.mappers.PhysicalPersonMapper;
import io.github.wendergalan.personapi.repositories.PhysicalPersonRepository;
import io.github.wendergalan.personapi.services.PhysicalPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PhysicalPersonServiceImpl implements PhysicalPersonService {

    @Autowired
    private PhysicalPersonRepository physicalPersonRepository;

    @Autowired
    private PhysicalPersonMapper physicalPersonMapper;

    @Override
    public ResponseEntity deleteById(int idPhysicalPerson) {
        physicalPersonRepository.deleteById(idPhysicalPerson);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity saveV1(PhysicalPersonDTOV1 physicalPersonDtoV1, BindingResult result) {
        if (result.hasErrors())
            return ResponseEntity.badRequest().body(Helper.criarListaDeErrosDaValidacao(result.getAllErrors()));

        PhysicalPerson physicalPerson = physicalPersonMapper.physicalPersonDTOV1ToPhysicalPerson(physicalPersonDtoV1);

        physicalPersonRepository.save(physicalPerson);

        // Add HATEOAS
        physicalPerson.add(linkTo(methodOn(PhysicalPersonController.class).saveV1(physicalPersonDtoV1, null)).withSelfRel().withType(HttpMethod.POST.toString()));
        physicalPerson.add(linkTo(methodOn(PhysicalPersonController.class).deleteById(physicalPerson.getId())).withRel("delete").withType(HttpMethod.DELETE.toString()));
        physicalPerson.add(linkTo(methodOn(PhysicalPersonController.class).getById(physicalPerson.getId())).withRel("details").withType(HttpMethod.GET.toString()));
        physicalPerson.add(linkTo(methodOn(PhysicalPersonController.class).findAllPeople()).withRel("find-all").withType(HttpMethod.GET.toString()));

        return ResponseEntity.status(HttpStatus.CREATED).body(physicalPerson);
    }

    @Override
    public ResponseEntity saveV2(PhysicalPersonDTOV2 physicalPersonDtoV2, BindingResult result) {
        if (result.hasErrors())
            return ResponseEntity.badRequest().body(Helper.criarListaDeErrosDaValidacao(result.getAllErrors()));

        PhysicalPerson physicalPerson = physicalPersonMapper.physicalPersonDTOV2PhysicalPerson(physicalPersonDtoV2);

        physicalPersonRepository.save(physicalPerson);

        // Add HATEOAS
        physicalPerson.add(linkTo(methodOn(PhysicalPersonController.class).saveV2(physicalPersonDtoV2, null)).withSelfRel().withType(HttpMethod.POST.toString()));
        physicalPerson.add(linkTo(methodOn(PhysicalPersonController.class).deleteById(physicalPerson.getId())).withRel("delete").withType(HttpMethod.DELETE.toString()));
        physicalPerson.add(linkTo(methodOn(PhysicalPersonController.class).getById(physicalPerson.getId())).withRel("details").withType(HttpMethod.GET.toString()));
        physicalPerson.add(linkTo(methodOn(PhysicalPersonController.class).findAllPeople()).withRel("find-all").withType(HttpMethod.GET.toString()));

        return ResponseEntity.status(HttpStatus.CREATED).body(physicalPerson);
    }

    @Override
    public ResponseEntity update(int idPhysicalPerson, PhysicalPerson physicalPerson) {
        physicalPerson.setId(idPhysicalPerson);

        // Add HATEOAS
        physicalPerson.add(linkTo(methodOn(PhysicalPersonController.class).getById(idPhysicalPerson)).withSelfRel().withType(HttpMethod.PUT.toString()));
        physicalPerson.add(linkTo(methodOn(PhysicalPersonController.class).deleteById(idPhysicalPerson)).withRel("delete").withType(HttpMethod.DELETE.toString()));
        physicalPerson.add(linkTo(methodOn(PhysicalPersonController.class).findAllPeople()).withRel("find-all").withType(HttpMethod.GET.toString()));

        physicalPersonRepository.save(physicalPerson);
        return ResponseEntity.ok(physicalPerson);
    }

    @Override
    public ResponseEntity getById(int idPhysicalPerson) {
        Optional<PhysicalPerson> pf = physicalPersonRepository.findById(idPhysicalPerson);

        if (!pf.isPresent())
            return ResponseEntity.notFound().build();

        // Add HATEOAS
        pf.get().add(linkTo(methodOn(PhysicalPersonController.class).getById(idPhysicalPerson)).withSelfRel().withType(HttpMethod.GET.toString()));
        pf.get().add(linkTo(methodOn(PhysicalPersonController.class).findAllPeople()).withRel("find-all").withType(HttpMethod.GET.toString()));
        pf.get().add(linkTo(methodOn(PhysicalPersonController.class).deleteById(idPhysicalPerson)).withRel("delete").withType(HttpMethod.DELETE.toString()));

        return ResponseEntity.ok(pf);
    }

    @Override
    public ResponseEntity findAllPeople() {
        List<PhysicalPerson> physicalPeople = physicalPersonRepository.findAll();
        physicalPeople.forEach(pf -> {
            // Add HATEOAS
            pf.add(linkTo(methodOn(PhysicalPersonController.class).findAllPeople()).withSelfRel().withType(HttpMethod.GET.toString()));
            pf.add(linkTo(methodOn(PhysicalPersonController.class).getById(pf.getId())).withRel("details").withType(HttpMethod.GET.toString()));
            pf.add(linkTo(methodOn(PhysicalPersonController.class).deleteById(pf.getId())).withRel("delete").withType(HttpMethod.DELETE.toString()));
            pf.add(linkTo(methodOn(PhysicalPersonController.class).update(pf.getId(), pf)).withRel("update").withType(HttpMethod.PUT.toString()));
        });
        return ResponseEntity.ok(physicalPeople);
    }

}
