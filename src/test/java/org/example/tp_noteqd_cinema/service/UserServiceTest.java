package org.example.tp_noteqd_cinema.service;

import org.example.tp_noteqd_cinema.domain.Utilisateur;
import org.example.tp_noteqd_cinema.dto.UserRegistrationDto;
import org.example.tp_noteqd_cinema.exception.BadRequestException;
import org.example.tp_noteqd_cinema.exception.NotFoundException;
import org.example.tp_noteqd_cinema.repository.UtilisateurRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("register saves new user when email unused")
    void register_ok() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setNom("Alice");
        dto.setEmail("alice@example.com");
        dto.setMotDePasse("secret");

        when(utilisateurRepository.findByEmail("alice@example.com")).thenReturn(Optional.empty());
        when(utilisateurRepository.save(any(Utilisateur.class)))
                .thenAnswer(invocation -> {
                    Utilisateur u = invocation.getArgument(0);
                    u.setId(1L);
                    return u;
                });

        Utilisateur created = userService.register(dto);

        assertThat(created.getId()).isEqualTo(1L);
        assertThat(created.getNom()).isEqualTo("Alice");
        assertThat(created.isCarteFideliteActive()).isFalse();

        ArgumentCaptor<Utilisateur> captor = ArgumentCaptor.forClass(Utilisateur.class);
        verify(utilisateurRepository).save(captor.capture());
        assertThat(captor.getValue().getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    @DisplayName("register throws when email already exists")
    void register_duplicateEmail_throws() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setNom("Alice");
        dto.setEmail("alice@example.com");
        dto.setMotDePasse("secret");

        when(utilisateurRepository.findByEmail("alice@example.com"))
                .thenReturn(Optional.of(Utilisateur.builder().id(99L).build()));

        assertThrows(BadRequestException.class, () -> userService.register(dto));
        verify(utilisateurRepository, never()).save(any());
    }

    @Test
    @DisplayName("buyLoyaltyCard activates card and sets +1 year expiration")
    void buyCard_ok() {
        Utilisateur u = Utilisateur.builder()
                .id(5L)
                .nom("Bob")
                .email("bob@example.com")
                .motDePasse("pwd")
                .carteFideliteActive(false)
                .build();
        when(utilisateurRepository.findById(5L)).thenReturn(Optional.of(u));
        when(utilisateurRepository.save(any(Utilisateur.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        String expectedExp = LocalDate.now().plusYears(1).toString();

        Utilisateur updated = userService.buyLoyaltyCard(5L);

        assertThat(updated.isCarteFideliteActive()).isTrue();
        assertEquals(expectedExp, updated.getCarteExpiration());
        verify(utilisateurRepository).save(updated);
    }

    @Test
    @DisplayName("buyLoyaltyCard throws when user not found")
    void buyCard_userNotFound() {
        when(utilisateurRepository.findById(123L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.buyLoyaltyCard(123L));
    }
}
