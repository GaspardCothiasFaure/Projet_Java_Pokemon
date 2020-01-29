package pokemon;
import java.util.Scanner;

/**
 * <b>Combatdresseur est la classe qui g�re le combat entre le dresseur et un PNJ </b>
 * <p>
 * Combatdresseur est caracteris� par les informations suivantes :
 * <ul>
 * <li>Le dresseur qui combat</li>
 * <li>L'adversaire qui est un PNJ combattant</li>
 * </ul>
 * </p>
 * <p>
 * De plus, l'entier pu(potion utilis�e par l'adversaire) qui est comptabilis�e
 * pour pouvoir r�initialiser le nombre de potions de l'adversaire.
 * </p>
 */

public class Combatdresseur {
	private Dresseur dresseur;
	private PNJcombattant adversaire;
	
	static int pu=0;
	
	public Dresseur getDresseur() {
		return dresseur;
	}
	public void setDresseur(Dresseur dresseur) {
		this.dresseur = dresseur;
	}
	public PNJcombattant getAdversaire() {
		return adversaire;
	}
	public void setAdversaire(PNJcombattant adversaire) {
		this.adversaire = adversaire;
	}

	
	public Combatdresseur(Dresseur dresseur, PNJcombattant adversaire) {
		 this.dresseur = dresseur;
		 this.adversaire = adversaire ;

	 }
	
	/**
     * Retourne un bool�en qui �tablit si les pokemons de l'adversaire
     * peuvent encore combattre.
     * 
     * @param adversaire: l'adversaire qui est un PNJcombattant.
     * 
     * @return un bool�en
     */
	public static boolean etatpokemonadverse(PNJcombattant adversaire) {

		for (Pokemon pok: adversaire.getListpokemon()) {
			if (pok.getPv() > 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
     * Retourne le pokemon qui sera utilis� en combat par l'adversaire.
     * 
     * @param adversaire: l'adversaire qui est un PNJcombattant.
     * 
     * @return soit le premier pokemon de l'adversaire qui est en �tat de combattre
     * soit le premier pokemon de la liste s'il n'y en a aucun.
     */
	public static Pokemon pokemonadverse(PNJcombattant adversaire) {
		for (Pokemon pok: adversaire.getListpokemon()) {
			if (pok.getPv() > 0) {
				System.out.println(adversaire.getName() + " lance :" + pok.getName() + " ," + pok.getPv()+ "/" + pok.getPv_max() + "PV");
				return pok;
			}
		}
		
		return adversaire.getListpokemon().get(0);
	}
	
	
	/**
     * Corps de la classe Combatdresseur qui r�utilise les m�thodes d�finies
     * pr�cedemment ainsi que celles de Combatpokemon.
     * 
     * @param dresseur: le dresseur du joueur
     * 
     * @param adversaire: le dresseur adversaire de la classe PNJcombattant
     * 
     * @return Le pseudo du membre, sous forme d'une chaine de caract�res.
     */
	public static void combatPNJ(Dresseur dresseur,PNJcombattant adversaire) {
		Scanner sc = new Scanner(System.in);
		int decisionint = Combatpokemon.combatoufuite();
		String decision = "0";
		if (decisionint == 1) {
			while (etatpokemonadverse(adversaire) && Combatpokemon.continuerlecombat(dresseur)) {
				
		
				Pokemon pokemonenjeu = Combatpokemon.pokemonenjeu(dresseur);

				Pokemon pokemonadverse = pokemonadverse(adversaire);
				while(pokemonenjeu.getPv() > 0 && pokemonadverse.getPv() > 0) {
					System.out.println("que voulez-vous faire ?");
					System.out.println("1: attaquer, 2: changer de pokemon, 3: soigner un pokemon");
					decision = sc.nextLine();
					while (!decision.equals("1") &&!decision.equals("2") &&!decision.equals("3")){
						System.out.println("mauvaise entrée, recommencez.");	
						System.out.println("que voulez-vous faire ?");
						System.out.println("1: attaquer, 2: changer de pokemon, 3: soigner un pokemon");
						decision = sc.nextLine();
					}
					decisionint=Integer.parseInt(decision);
			
					if( decisionint == 1){
						decisionint = Combatpokemon.attaqueoudefense();
						if(decisionint ==1) {
							/**
						     * Le pokemon dont la vitesse est la plus �lev�e attaque en premier 
						     */
							if(pokemonenjeu.getVitesse() >= pokemonadverse.getVitesse()){
								Combatpokemon.attaquepokemonenjeu(pokemonenjeu,pokemonadverse);
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if (pokemonadverse.getPv() <= 0) {
									System.out.println(pokemonadverse.getName() + " est Ko" );
									pokemonenjeu.setXp(pokemonenjeu.getXp() + pokemonadverse.getLvl());
								}
								
								/**
							     * Lorsque l'Xp du pokemon du joueur augmente, on v�rifie s'il peut monter de niveau
							     * puis ensuite on v�rifie s'il peut �voluer, si c'est le cas, le pokemon est remplac� par 
							     * le pokemon d'identifiant suivant car les pokemons ont des identifiants qui se suivent
							     * par �volution
							     */
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								Pokemon.levelup(pokemonenjeu);
								if (pokemonenjeu.isEvoluable()) {
									if(pokemonenjeu.getLvl()>pokemonenjeu.getLvlnecessaire()) {
										for (Pokemon p: NewMain.ListePokemon) {
											if (p.getId()==pokemonenjeu.getId()+1) {
												dresseur.getListpokemon().set(dresseur.getListpokemon().indexOf(pokemonenjeu), p);
											}
											
										}
									}
									
								}
								if (pokemonadverse.getPv() > 0) {	
									double ia=Math.random();
									/**
								     * Le double ia va d�terminer l'action � venir de l'adversaire par des probabilit�s
								     * Pour augmenter la difficult� du jeu, l'adversaire � la diff�rence du joueur peut
								     * dans le m�me tour soigner son pokemon et attaquer.
								     */
									if (ia>=0.5 && adversaire.getPotion()>0 && pokemonadverse.getPv()<(pokemonadverse.getPv()/2)){
										pokemonadverse.setPv(pokemonadverse.getPv_max());
										adversaire.setPotion(adversaire.getPotion()-1);
										pu=pu+1;
										System.out.println(adversaire.getName()+ "utilise une potion sur "+ pokemonadverse.getName());
									}
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									if (ia<=0.7) {
										Combatpokemon.attaquepokemonadverse(pokemonenjeu,pokemonadverse);
									}
									else {
										Combatpokemon.defensepokemonadverse(pokemonenjeu,pokemonadverse);
									}
									
								}
								
							}
							else{
								double ia=Math.random();
								if (ia>=0.5 && adversaire.getPotion()>0 && pokemonadverse.getPv()<(pokemonadverse.getPv()/2)){
									pokemonadverse.setPv(pokemonadverse.getPv_max());
									adversaire.setPotion(adversaire.getPotion()-1);
									pu=pu+1;
									System.out.println(adversaire.getName()+ "utilise une potion sur "+ pokemonadverse.getName());
								}
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if (ia<=0.7) {
									Combatpokemon.attaquepokemonadverse(pokemonenjeu,pokemonadverse);
								}
								else {
									Combatpokemon.defensepokemonadverse(pokemonenjeu,pokemonadverse);
								}
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if (pokemonenjeu.getPv() <= 0) {
									System.out.println(pokemonenjeu.getName() + " est Ko" );
									pokemonenjeu.setXp(pokemonenjeu.getXp() + pokemonadverse.getLvl());
								}
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if (pokemonenjeu.getPv() > 0) {	
									Combatpokemon.attaquepokemonenjeu(pokemonenjeu,pokemonadverse);
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									if(pokemonadverse.getPv() <= 0) {
										pokemonenjeu.setXp(pokemonenjeu.getXp() + pokemonadverse.getLvl());
										System.out.println(pokemonenjeu.getName() + " gagne " + pokemonadverse.getLvl() + "points d'xp");
									}
								}
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								Pokemon.levelup(pokemonenjeu);
								if (pokemonenjeu.isEvoluable()) {
									if(pokemonenjeu.getLvl()>pokemonenjeu.getLvlnecessaire()) {
										for (Pokemon p: NewMain.ListePokemon) {
											if (p.getId()==pokemonenjeu.getId()+1) {
												dresseur.getListpokemon().set(dresseur.getListpokemon().indexOf(pokemonenjeu), p);
												System.out.println("F�licitation votre" + pokemonenjeu.getName()+ "a �volu� en" + p.getName());
											}
										}
									}
								}
							}
						}
						else {
							if(pokemonenjeu.getVitesse() >= pokemonadverse.getVitesse()){
								Combatpokemon.defensepokemonenjeu(pokemonenjeu, pokemonadverse);
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if (pokemonadverse.getPv() > 0) {
									double ia=Math.random();
									if (ia>=0.5 && adversaire.getPotion()>0 && pokemonadverse.getPv()<(pokemonadverse.getPv()/2)){
										pokemonadverse.setPv(pokemonadverse.getPv_max());
										adversaire.setPotion(adversaire.getPotion()-1);
										pu=pu+1;
										System.out.println(adversaire.getName()+ "utilise une potion sur "+ pokemonadverse.getName());
									}
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									if (ia<=0.7) {
										Combatpokemon.attaquepokemonadverse(pokemonenjeu,pokemonadverse);
									}
									else {
										Combatpokemon.defensepokemonadverse(pokemonenjeu,pokemonadverse);
									}
							}
						}
							else {
								double ia=Math.random();
								if (ia>=0.5 && adversaire.getPotion()>0 && pokemonadverse.getPv()<(pokemonadverse.getPv()/2)){
									pokemonadverse.setPv(pokemonadverse.getPv_max());
									adversaire.setPotion(adversaire.getPotion()-1);
									pu=pu+1;
									System.out.println(adversaire.getName()+ "utilise une potion sur "+ pokemonadverse.getName());
								}
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if (ia<=0.7) {
									Combatpokemon.attaquepokemonadverse(pokemonenjeu,pokemonadverse);
								}
								else {
									Combatpokemon.defensepokemonadverse(pokemonenjeu,pokemonadverse);
								}
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if (pokemonenjeu.getPv() <= 0) {
									System.out.println(pokemonenjeu.getName() + " est Ko" );
									pokemonenjeu.setXp(pokemonenjeu.getXp() + pokemonadverse.getLvl());
								}
								
								if (pokemonenjeu.getPv() > 0) {	
									Combatpokemon.defensepokemonenjeu(pokemonenjeu, pokemonadverse);
								}
							}
					}
					}
					
					else if(decisionint == 2) {
						pokemonenjeu = Combatpokemon.pokemonenjeu(dresseur);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						double ia=Math.random();
						if (ia>=0.5 && adversaire.getPotion()>0 && pokemonadverse.getPv()<(pokemonadverse.getPv()/2)){
							pokemonadverse.setPv(pokemonadverse.getPv_max());
							adversaire.setPotion(adversaire.getPotion()-1);
							pu=pu+1;
							System.out.println(adversaire.getName()+ "utilise une potion sur "+ pokemonadverse.getName());
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (ia<=0.7) {
							Combatpokemon.attaquepokemonadverse(pokemonenjeu,pokemonadverse);
						}
						else {
							Combatpokemon.defensepokemonadverse(pokemonenjeu,pokemonadverse);
						}
					}
					
					else if(decisionint == 3) {
						if(dresseur.getPotion() >  0) {
							Combatpokemon.soignerpokemon(dresseur);
							try {
								Thread.sleep(1500);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							double ia=Math.random();
							if (ia>=0.5 && adversaire.getPotion()>0 && pokemonadverse.getPv()<(pokemonadverse.getPv()/2)){
								pokemonadverse.setPv(pokemonadverse.getPv_max());
								adversaire.setPotion(adversaire.getPotion()-1);
								pu=pu+1;
								System.out.println(adversaire.getName()+ "utilise une potion sur "+ pokemonadverse.getName());
							}
							try {
								Thread.sleep(1500);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (ia<=0.7) {
								Combatpokemon.attaquepokemonadverse(pokemonenjeu,pokemonadverse);
							}
							else {
								Combatpokemon.defensepokemonadverse(pokemonenjeu,pokemonadverse);
							}
						}						
					}
					else {
						System.out.println("Vous n'avez pas de potion!");
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (pokemonadverse.getPv() <= 0 && etatpokemonadverse(adversaire) && Combatpokemon.continuerlecombat(dresseur)) {
					pokemonadverse = pokemonadverse(adversaire);
				}
				else if (pokemonenjeu.getPv() <=0 && etatpokemonadverse(adversaire) && Combatpokemon.continuerlecombat(dresseur)) {
					pokemonenjeu = Combatpokemon.pokemonenjeu(dresseur);
				}
			}
			if(etatpokemonadverse(adversaire) == false ) {
				for (Pokemon p: dresseur.getListpokemon()) {
					Combatpokemon.restaurer(p);
				}
				for (Pokemon p: adversaire.getListpokemon()) {
					Combatpokemon.restaurer(p);
					p.setPv(p.getPv_max());
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Vous avez battu " + adversaire.getName());
				System.out.println("Vous avez gagné 10 ects." );
				dresseur.setArgent(dresseur.getArgent()+10);
				/**
			     * Si l'adversaire battu par le joueur �tait l'un des trois champions d'ar�ne alors 
			     * le joueur gagne un badge.
			     */
				if(adversaire.getName() == "Serge_botton" || adversaire.getName() == "Abelatif" || adversaire.getName() == "Benoit_coste") {
					dresseur.setNb_badge(dresseur.getNb_badge()+1);
					System.out.println("Vous avez gagn� un badge d'ar�ne.");
				}
			}
			else {
				for (Pokemon p: dresseur.getListpokemon()) {
					Combatpokemon.restaurer(p);
				}
				for (Pokemon p: adversaire.getListpokemon()) {
					Combatpokemon.restaurer(p);
					p.setPv(p.getPv_max());
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Vous avez perdu le combat");
				System.out.println("Direction le centre MRS pour soigner vos pokemons");
				dresseur.setPositionx(0);
				dresseur.setPositiony(0);
			}
		}
		
		
		else if (decisionint == 2) {
			System.out.println("Vous prenez la fuite.");
			for (Pokemon p: dresseur.getListpokemon()) {
				Combatpokemon.restaurer(p);
			}
			for (Pokemon p: adversaire.getListpokemon()) {
				Combatpokemon.restaurer(p);
				p.setPv(p.getPv_max());
			}
		}
	}
	}