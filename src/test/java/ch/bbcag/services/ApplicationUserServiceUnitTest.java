package ch.bbcag.services;

import ch.bbcag.models.ApplicationUser;
import ch.bbcag.repositories.ApplicationUserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static ch.bbcag.utils.TestDataUtil.getTestUser;
import static ch.bbcag.utils.TestDataUtil.getTestUsers;
import static org.mockito.Mockito.doReturn;

public class ApplicationUserServiceUnitTest {

    private final ApplicationUserRepository applicationUserRepository = Mockito.mock(ApplicationUserRepository.class);
    private final BCryptPasswordEncoder bCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
    private final ApplicationUserService applicationUserService = new ApplicationUserService(applicationUserRepository, bCryptPasswordEncoder);

    @Test
    public void checkSignUp_whenValidUser_thenUserPasswordIsHashed() {
        String hashedPassword = "hashedPassword";
        ApplicationUser user = getTestUser();
        doReturn(hashedPassword).when(bCryptPasswordEncoder).encode(user.getPassword());

        applicationUserService.signUp(user);

        Assertions.assertThat(user.getPassword()).isEqualTo(hashedPassword);
        Mockito.verify(applicationUserRepository).save(Mockito.eq(user));
    }

    @Test
    public void checkFindAll_whenExecute_thenUsersAreReturned() {
        doReturn(getTestUsers()).when(applicationUserRepository).findAll();
        List<ApplicationUser> applicationUsers = (List<ApplicationUser>) applicationUserService.findAll();
        Assertions.assertThat(applicationUsers.size()).isEqualTo(4);
    }

    @Test
    public void checkFindById_whenValidId_thenUserIsReturned() {
        ApplicationUser expected = getTestUser();
        doReturn(Optional.of(expected)).when(applicationUserRepository).findById(1);

        ApplicationUser found = applicationUserService.findById(expected.getId());
        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat(found).isEqualTo(expected);
    }

    @Test
    public void checkFindById_whenInvalidId_thenReturnEmptyOptional() {
        Assertions.assertThatThrownBy(()->applicationUserService.findById(null)).hasCauseInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void checkUpdate_whenValidUser_thenUserPasswordIsHashed() {
        String hashedPassword = "hashedPassword";
        ApplicationUser user = getTestUser();
        doReturn(hashedPassword).when(bCryptPasswordEncoder).encode(user.getPassword());

        applicationUserService.update(user);

        Assertions.assertThat(user.getPassword()).isEqualTo(hashedPassword);
        Mockito.verify(applicationUserRepository).save(Mockito.eq(user));
    }

    @Test
    public void checkDelete_whenValidId_thenIdIsDeleted() {
        applicationUserService.deleteById(1);
        Mockito.verify(applicationUserRepository).deleteById(Mockito.eq(1));
    }

}
