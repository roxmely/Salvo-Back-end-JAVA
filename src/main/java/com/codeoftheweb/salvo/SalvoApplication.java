package com.codeoftheweb.salvo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@SpringBootApplication
public class SalvoApplication  {


	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Autowired
	PasswordEncoder passwordEncoder;

	//    @Bean
//    public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository){
//        return (args) -> {	// save a couple of customers

						//****Date****//

			/**Calendar dateGames = Calendar.getInstance();
			dateGames.setTime(new Date());

					//******Info-Game******/

			/**Game game1 = new Game();
			dateGames.add(Calendar.HOUR_OF_DAY, 2);
			Game game2= new Game(dateGames.getTime());
			dateGames.add(Calendar.HOUR_OF_DAY, 2);
			Game game3 = new Game(dateGames.getTime());
			dateGames.add(Calendar.HOUR_OF_DAY, 2);
			Game game4 = new Game(dateGames.getTime());
			dateGames.add(Calendar.HOUR_OF_DAY, 2);
			Game game5 = new Game(dateGames.getTime());
			dateGames.add(Calendar.HOUR_OF_DAY, 2);
			Game game6 = new Game(dateGames.getTime());
			dateGames.add(Calendar.HOUR_OF_DAY, 2);
			Game game7 = new Game(dateGames.getTime());
			dateGames.add(Calendar.HOUR_OF_DAY, 2);
			Game game8 = new Game(dateGames.getTime());
			dateGames.add(Calendar.HOUR_OF_DAY, 2);
					gameRepository.save(game1 );
					gameRepository.save(game2);
					gameRepository.save(game3);
					gameRepository.save(game4);
					gameRepository.save(game5 );
					gameRepository.save(game6);
					gameRepository.save(game7);
					gameRepository.save(game8);

				//*******Info-Player*****/
			/**Player jbauer=new Player("j.bauer@ctu.gov", passwordEncoder.encode("24"));
			Player cobrian=new Player("c.obrian@ctu.gov", passwordEncoder.encode("42"));
			Player kimbauer=new Player("kim_bauer@gmail.com", passwordEncoder.encode("kb"));
			Player talmeida=new Player(	"t.almeida@ctu.gov", passwordEncoder.encode("mole"));

			playerRepository.save(jbauer);
			playerRepository.save(cobrian);
			playerRepository.save(kimbauer);
			playerRepository.save(talmeida);

			         //********Ingo Game-Player********/

		/**	GamePlayer jbauerG1 = new GamePlayer(jbauer ,game1);
			GamePlayer cobrianG1 = new GamePlayer(cobrian ,game1);

			GamePlayer jbauerG2 = new GamePlayer(jbauer,game2);
			GamePlayer cobrianG2  = new GamePlayer(cobrian,game2);

			GamePlayer cobrianG3 = new GamePlayer(cobrian, game3);
			GamePlayer talmeidaG3 = new GamePlayer(talmeida, game3);

			GamePlayer cobrianG4 = new GamePlayer(cobrian, game4);
			GamePlayer jbauerG4 = new GamePlayer(jbauer, game4);

			GamePlayer talmeidaG5 = new GamePlayer(talmeida ,game5);
			GamePlayer jbauerG5 = new GamePlayer(jbauer ,game5);

			GamePlayer kimbauerG6 = new GamePlayer(kimbauer,game6);

			GamePlayer talmeidaG7 = new GamePlayer(talmeida, game7);

			GamePlayer kimbauerG8 = new GamePlayer(kimbauer, game8);

			GamePlayer talmeidaG8 = new GamePlayer(talmeida, game8);

						gamePlayerRepository.save(jbauerG1);
						gamePlayerRepository.save(cobrianG1);
						gamePlayerRepository.save(jbauerG2);
						gamePlayerRepository.save(cobrianG2);
						gamePlayerRepository.save(cobrianG3);
						gamePlayerRepository.save(talmeidaG3);
						gamePlayerRepository.save(cobrianG4);
						gamePlayerRepository.save(jbauerG4);
						gamePlayerRepository.save(talmeidaG5);
						gamePlayerRepository.save(jbauerG5);
						gamePlayerRepository.save(kimbauerG6);
						gamePlayerRepository.save(talmeidaG7);
						gamePlayerRepository.save(kimbauerG8);
						gamePlayerRepository.save(talmeidaG8);

			//********Info Ships********/

		/**	Ship ship1 =new Ship("Destroyer",jbauerG1, Arrays.asList("H2", "H3", "H4"));
			Ship ship2 =new Ship("Submarine",jbauerG1, Arrays.asList("E1", "F1", "G1"));
			Ship ship3=new Ship("Patrol Boat",jbauerG1,  Arrays.asList("B4", "B5"));
			Ship ship4=new Ship("Destroyer",cobrianG1,  Arrays.asList("B5", "C5", "D5"));
			Ship ship5 =new Ship("Patrol Boat",cobrianG1, Arrays.asList("F1", "F2"));
			Ship ship6 =new Ship("Submarine",jbauerG2, Arrays.asList("B5", "C5", "D5"));
			Ship ship7 =new Ship("Patrol Boat",jbauerG2, Arrays.asList("C6", "C7"));
			Ship ship8 =new Ship("Destroyer",cobrianG2, Arrays.asList("A2", "A3", "A4"));
			Ship ship9=new Ship("Patrol Boat",cobrianG2,  Arrays.asList("G6", "H6"));
			Ship ship10=new Ship("Submarine",cobrianG3, Arrays.asList("B5", "C5", "D5"));
			Ship ship11 =new Ship("Patrol Boat",cobrianG3, Arrays.asList("C6", "C7"));
			Ship ship12 =new Ship("Destroyer",talmeidaG3, Arrays.asList("A2", "A3", "A4"));
			Ship ship13 =new Ship("Patrol Boat",talmeidaG3, Arrays.asList("G6", "H6"));
			Ship ship14 =new Ship("Submarine",cobrianG4, Arrays.asList("B5", "C5", "D5"));
			Ship ship15 =new Ship("Submarine",cobrianG4, Arrays.asList("C6", "C7"));
			Ship ship16 =new Ship("Patrol Boat",jbauerG4, Arrays.asList("A2", "A3", "A4"));
			Ship ship17 =new Ship("Destroyer",jbauerG4, Arrays.asList("G6", "H6"));
			Ship ship18 =new Ship("Patrol Boat ",talmeidaG5, Arrays.asList("B5", "C5", "D5"));
			Ship ship19 =new Ship("Submarine",talmeidaG5, Arrays.asList("C6", "C7"));
			Ship ship20 =new Ship("Patrol Boat",jbauerG5, Arrays.asList("A2", "A3", "A4"));
			Ship ship21 =new Ship("Submarine",jbauerG5, Arrays.asList("G6", "H6"));
			Ship ship22 =new Ship("Destroyer",kimbauerG6, Arrays.asList("B5", "C5", "D5"));
			Ship ship23 =new Ship("Patrol Boat",kimbauerG6, Arrays.asList("C6", "C7"));
			Ship ship24 =new Ship("Submarine",kimbauerG8, Arrays.asList("B5", "C5", "D5"));
			Ship ship25 =new Ship("Patrol Boat",kimbauerG8, Arrays.asList("C6", "C7"));
			Ship ship26 =new Ship("Patrol Boat",talmeidaG8, Arrays.asList("A2", "A3", "A4"));
			Ship ship27 =new Ship("Patrol Boat",talmeidaG8, Arrays.asList("G6", "H6"));

								shipRepository.save(ship1);
								shipRepository.save(ship2);
								shipRepository.save(ship3);
								shipRepository.save(ship4);
								shipRepository.save(ship5);
								shipRepository.save(ship6);
								shipRepository.save(ship7);
								shipRepository.save(ship8);
								shipRepository.save(ship9);
								shipRepository.save(ship10);
								shipRepository.save(ship11);
								shipRepository.save(ship12);
								shipRepository.save(ship13);
								shipRepository.save(ship14);
								shipRepository.save(ship15);
								shipRepository.save(ship16);
								shipRepository.save(ship17);
								shipRepository.save(ship18);
								shipRepository.save(ship19);
								shipRepository.save(ship20);
								shipRepository.save(ship21);
								shipRepository.save(ship22);
								shipRepository.save(ship23);
								shipRepository.save(ship24);
								shipRepository.save(ship25);
								shipRepository.save(ship26);
								shipRepository.save(ship27);

			Salvo salvo =new Salvo( jbauerG1,1,Arrays.asList("B5", "C5", "F1"));
			Salvo salvo1 =new Salvo(cobrianG1,1,Arrays.asList("B4", "B5", "B6"));
			Salvo salvo2 =new Salvo(jbauerG1,2,Arrays.asList("F2", "D5"));
			Salvo salvo3 =new Salvo(cobrianG1,2,Arrays.asList("E1", "H3","A2"));
			Salvo salvo4 =new Salvo(jbauerG2,1,Arrays.asList("A2", "A4", "G6" ));
			Salvo salvo5 =new Salvo(cobrianG2,1,Arrays.asList("B5", "D5", "C7"));
			Salvo salvo6 =new Salvo(jbauerG2,2,Arrays.asList("A3", "H6"));
			Salvo salvo7 =new Salvo(cobrianG2,2,Arrays.asList("C5", "C6"));
			Salvo salvo8 =new Salvo(cobrianG3,1,Arrays.asList("G6", "H6", "A4"));
			Salvo salvo9 =new Salvo(talmeidaG3,1,Arrays.asList("H1", "H2", "H3"));
			Salvo salvo10 =new Salvo(cobrianG3,2,Arrays.asList("A2", "A3", "D8"));
			Salvo salvo11 =new Salvo(talmeidaG3,2,Arrays.asList("E1", "F2", "G3"));
			Salvo salvo12 =new Salvo(cobrianG4,1,Arrays.asList("A3", "A4", "F7"));
			Salvo salvo13 =new Salvo(jbauerG4,1,Arrays.asList("B5", "C6", "H1"));
			Salvo salvo14 =new Salvo(cobrianG4,2,Arrays.asList("A2", "G6", "H6"));
			Salvo salvo15 =new Salvo(jbauerG4,2,Arrays.asList("C5", "C7", "D5"));
			Salvo salvo16 =new Salvo(talmeidaG5,1,Arrays.asList("A1", "A2", "A3"));
			Salvo salvo17 =new Salvo(jbauerG5,1,Arrays.asList("B5", "B6", "C7"));
			Salvo salvo18 =new Salvo(talmeidaG5,2,Arrays.asList("G6", "G7", "G8"));
			Salvo salvo19 =new Salvo(jbauerG5,2,Arrays.asList("C6", "D6", "E6"));
			Salvo salvo20 =new Salvo(jbauerG5,3,Arrays.asList("H1", "H8"));


									salvoRepository.save(salvo);
									salvoRepository.save(salvo1);
									salvoRepository.save(salvo2);
									salvoRepository.save(salvo3);
									salvoRepository.save(salvo4);
									salvoRepository.save(salvo5);
									salvoRepository.save(salvo6);
									salvoRepository.save(salvo7);
									salvoRepository.save(salvo8);
									salvoRepository.save(salvo9);
									salvoRepository.save(salvo10);
									salvoRepository.save(salvo11);
									salvoRepository.save(salvo12);
									salvoRepository.save(salvo13);
									salvoRepository.save(salvo14);
									salvoRepository.save(salvo15);
									salvoRepository.save(salvo16);
									salvoRepository.save(salvo17);
									salvoRepository.save(salvo18);
									salvoRepository.save(salvo19);
									salvoRepository.save(salvo20);


			Score jbauerp1 =new Score( 1.0 ,game1,jbauer);
			Score cobrianp1 =new Score( 0.0 ,game1,cobrian);
			Score jbauerp2 =new Score( 0.5 ,game2,jbauer);
			Score cobrianp2 =new Score( 0.5 ,game2,cobrian);
			Score cobrianp3 =new Score( 1.0 ,game3,cobrian);
			Score talmeidap3 =new Score( 0.0 ,game3,talmeida);
			Score cobrianp4 =new Score( 0.5 ,game4,cobrian);
			Score jbauerp4 =new Score( 0.5,game4,jbauer);

			scoreRepository.save(jbauerp1);
			scoreRepository.save(cobrianp1);
			scoreRepository.save(jbauerp2);
			scoreRepository.save(cobrianp2);
			scoreRepository.save(cobrianp3);
			scoreRepository.save(talmeidap3);
			scoreRepository.save(cobrianp4);
			scoreRepository.save(jbauerp4);


		};
	}

}**/
}

@Configuration
@EnableWebSecurity
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	private PlayerRepository playerRepository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName-> {
			Player player = playerRepository.findByEmail(inputName);
			if (player != null) {
				return new User(player.getEmail(), player.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + inputName);
			}
		});
	}
}
@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors();
		http.authorizeRequests()
				.antMatchers("/web/games.html","/api/leaderboard","/api/games","/api/players","/api/status").permitAll()
				.antMatchers("/api/game_view/").hasAuthority("USER")
				.antMatchers("/web/game/gp/**.html").hasAuthority("USER")
				.anyRequest().fullyAuthenticated();
		http.formLogin()
				.usernameParameter("email").
				passwordParameter("password")
				.loginPage("/api/login").and();

		http.headers().frameOptions().disable();
				//LOGOUT
		http.logout().logoutUrl("/api/logout");

		// turn off checking for CSRF tokens
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();
		// The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("HEAD",
				"GET", "POST", "PUT", "DELETE", "PATCH"));
		// setAllowCredentials(true) is important, otherwise:
		// will fail with 403 Invalid CORS request
		configuration.setAllowCredentials(true);
		// setAllowedHeaders is important! Without it, OPTIONS preflight request
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}