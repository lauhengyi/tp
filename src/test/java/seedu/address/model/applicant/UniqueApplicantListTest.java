package seedu.address.model.applicant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.applicant.exceptions.DuplicatePersonException;
import seedu.address.model.applicant.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class UniqueApplicantListTest {

    private final UniqueApplicantList uniqueApplicantList = new UniqueApplicantList();

    @Test
    public void contains_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueApplicantList.contains(null));
    }

    @Test
    public void contains_personNotInList_returnsFalse() {
        assertFalse(uniqueApplicantList.contains(ALICE));
    }

    @Test
    public void contains_personInList_returnsTrue() {
        uniqueApplicantList.add(ALICE);
        assertTrue(uniqueApplicantList.contains(ALICE));
    }

    @Test
    public void contains_personWithSameIdentityFieldsInList_returnsTrue() {
        uniqueApplicantList.add(ALICE);
        Applicant editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(uniqueApplicantList.contains(editedAlice));
    }

    @Test
    public void add_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueApplicantList.add(null));
    }

    @Test
    public void add_duplicatePerson_throwsDuplicatePersonException() {
        uniqueApplicantList.add(ALICE);
        assertThrows(DuplicatePersonException.class, () -> uniqueApplicantList.add(ALICE));
    }

    @Test
    public void setPerson_nullTargetPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueApplicantList.setPerson(null, ALICE));
    }

    @Test
    public void setPerson_nullEditedPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueApplicantList.setPerson(ALICE, null));
    }

    @Test
    public void setPerson_targetPersonNotInList_throwsPersonNotFoundException() {
        assertThrows(PersonNotFoundException.class, () -> uniqueApplicantList.setPerson(ALICE, ALICE));
    }

    @Test
    public void setPerson_editedPersonIsSamePerson_success() {
        uniqueApplicantList.add(ALICE);
        uniqueApplicantList.setPerson(ALICE, ALICE);
        UniqueApplicantList expectedUniqueApplicantList = new UniqueApplicantList();
        expectedUniqueApplicantList.add(ALICE);
        assertEquals(expectedUniqueApplicantList, uniqueApplicantList);
    }

    @Test
    public void setPerson_editedPersonHasSameIdentity_success() {
        uniqueApplicantList.add(ALICE);
        Applicant editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        uniqueApplicantList.setPerson(ALICE, editedAlice);
        UniqueApplicantList expectedUniqueApplicantList = new UniqueApplicantList();
        expectedUniqueApplicantList.add(editedAlice);
        assertEquals(expectedUniqueApplicantList, uniqueApplicantList);
    }

    @Test
    public void setPerson_editedPersonHasDifferentIdentity_success() {
        uniqueApplicantList.add(ALICE);
        uniqueApplicantList.setPerson(ALICE, BOB);
        UniqueApplicantList expectedUniqueApplicantList = new UniqueApplicantList();
        expectedUniqueApplicantList.add(BOB);
        assertEquals(expectedUniqueApplicantList, uniqueApplicantList);
    }

    @Test
    public void setPerson_editedPersonHasNonUniqueIdentity_throwsDuplicatePersonException() {
        uniqueApplicantList.add(ALICE);
        uniqueApplicantList.add(BOB);
        assertThrows(DuplicatePersonException.class, () -> uniqueApplicantList.setPerson(ALICE, BOB));
    }

    @Test
    public void remove_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueApplicantList.remove(null));
    }

    @Test
    public void remove_personDoesNotExist_throwsPersonNotFoundException() {
        assertThrows(PersonNotFoundException.class, () -> uniqueApplicantList.remove(ALICE));
    }

    @Test
    public void remove_existingPerson_removesPerson() {
        uniqueApplicantList.add(ALICE);
        uniqueApplicantList.remove(ALICE);
        UniqueApplicantList expectedUniqueApplicantList = new UniqueApplicantList();
        assertEquals(expectedUniqueApplicantList, uniqueApplicantList);
    }

    @Test
    public void setPersons_nullUniqueApplicantList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueApplicantList.setPersons((UniqueApplicantList) null));
    }

    @Test
    public void setPersons_uniqueApplicantList_replacesOwnListWithProvidedUniqueApplicantList() {
        uniqueApplicantList.add(ALICE);
        UniqueApplicantList expectedUniqueApplicantList = new UniqueApplicantList();
        expectedUniqueApplicantList.add(BOB);
        uniqueApplicantList.setPersons(expectedUniqueApplicantList);
        assertEquals(expectedUniqueApplicantList, uniqueApplicantList);
    }

    @Test
    public void setPersons_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueApplicantList.setPersons((List<Applicant>) null));
    }

    @Test
    public void setPersons_list_replacesOwnListWithProvidedList() {
        uniqueApplicantList.add(ALICE);
        List<Applicant> applicantList = Collections.singletonList(BOB);
        uniqueApplicantList.setPersons(applicantList);
        UniqueApplicantList expectedUniqueApplicantList = new UniqueApplicantList();
        expectedUniqueApplicantList.add(BOB);
        assertEquals(expectedUniqueApplicantList, uniqueApplicantList);
    }

    @Test
    public void setPersons_listWithDuplicatePersons_throwsDuplicatePersonException() {
        List<Applicant> listWithDuplicateApplicants = Arrays.asList(ALICE, ALICE);
        assertThrows(DuplicatePersonException.class, () -> uniqueApplicantList.setPersons(listWithDuplicateApplicants));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
            -> uniqueApplicantList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueApplicantList.asUnmodifiableObservableList().toString(), uniqueApplicantList.toString());
    }

    @Test
    public void sortPersons_nameCaseSensitive_success() {
        Email email = new Email("123@gmail.com");
        Phone phone = new Phone("1234567890");
        JobPosition jp = new JobPosition("job");
        Status status = new Status("status");
        Address address = new Address("happy street");
        LocalDateTime addedTime = LocalDateTime.now();
        Set<Tag> tags = new HashSet<>();
        Applicant p1 = new Applicant(new Name("Aa"), phone, email, jp, status, address, addedTime, tags);
        Applicant p2 = new Applicant(new Name("aaa"), phone, email, jp, status, address, addedTime, tags);
        Applicant p3 = new Applicant(new Name("Bbb"), phone, email, jp, status, address, addedTime, tags);
        Applicant p4 = new Applicant(new Name("bbB"), phone, email, jp, status, address, addedTime, tags);
        uniqueApplicantList.add(p3);
        uniqueApplicantList.add(p2);
        uniqueApplicantList.add(p1);
        uniqueApplicantList.add(p4);

        UniqueApplicantList expectedList = new UniqueApplicantList();
        expectedList.add(p1); // Aa
        expectedList.add(p2); // aaa
        expectedList.add(p3); // Bbb
        expectedList.add(p4); // bbB

        uniqueApplicantList.sortPersonsByAscendingOrder(PREFIX_NAME);
        assertEquals(expectedList, uniqueApplicantList);
    }

    @Test
    public void sortPersons_nameLexOnly_failure() {
        Email email = new Email("123@gmail.com");
        Phone phone = new Phone("1234567890");
        JobPosition jp = new JobPosition("job");
        Status status = new Status("status");
        Address address = new Address("happy street");
        LocalDateTime addedTime = LocalDateTime.now();
        Set<Tag> tags = new HashSet<>();
        Applicant p1 = new Applicant(new Name("Aa"), phone, email, jp, status, address, addedTime, tags);
        Applicant p2 = new Applicant(new Name("aaa"), phone, email, jp, status, address, addedTime, tags);
        Applicant p3 = new Applicant(new Name("Bbb"), phone, email, jp, status, address, addedTime, tags);
        Applicant p4 = new Applicant(new Name("bbB"), phone, email, jp, status, address, addedTime, tags);
        uniqueApplicantList.add(p3);
        uniqueApplicantList.add(p2);
        uniqueApplicantList.add(p1);
        uniqueApplicantList.add(p4);

        UniqueApplicantList wronglySortedList = new UniqueApplicantList();
        // Error: lexicographical only
        wronglySortedList.add(p1); // Aa
        wronglySortedList.add(p3); // Bbb
        wronglySortedList.add(p2); // aaa
        wronglySortedList.add(p4); // bbB

        uniqueApplicantList.sortPersons(PREFIX_NAME);
        assertNotEquals(wronglySortedList, uniqueApplicantList);
    }



}
