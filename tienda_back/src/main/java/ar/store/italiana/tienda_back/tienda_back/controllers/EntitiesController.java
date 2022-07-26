package ar.store.italiana.tienda_back.tienda_back.controllers;

import ar.store.italiana.tienda_back.tienda_back.models.Dress;
import ar.store.italiana.tienda_back.tienda_back.models.Favorito;
import ar.store.italiana.tienda_back.tienda_back.models.Fragrance;
import ar.store.italiana.tienda_back.tienda_back.models.Product;
import ar.store.italiana.tienda_back.tienda_back.services.DressService;
import ar.store.italiana.tienda_back.tienda_back.services.FavoritoService;
import ar.store.italiana.tienda_back.tienda_back.services.FragranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//import ar.store.italiana.tienda_back.tienda_back.models.User;
//import ar.store.italiana.tienda_back.tienda_back.services.UserService;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


//	IMPORTANTE (DUDA)

	// SE PUEDE USAR UN SOLO CONTROLADOR PARA LAS 3 ENTIDADES, PERO TAL VEZ POR MOTIVOS DE ORDEN SE PUEDE HACER UNA MAS PARA LAS DOS CATEGORIAS


@RestController
@CrossOrigin
@RequestMapping("/interface")
public class EntitiesController {
	@Autowired
	private DressService dservice;
	@Autowired
	private FragranceService fservice;
	@Autowired
	private FavoritoService favservice;
	
	//DRESS SECTOR
	@GetMapping("/dress/all")
	public List<Dress> getAllDresses(){
		return dservice.getAllDresses();
	}
	@GetMapping("/dress/everything")
	public List<Dress> getEverythingOnDress(){
		return dservice.getEverything();
	}
	@GetMapping("dress/obj/{codigo}")
	public Dress getObjD(@PathVariable String codigo) {
		return dservice.getObjD(codigo);
	}
	@GetMapping("dress/size/{codigo}")
	public List<Dress> getSizeD(@PathVariable String codigo){
		return dservice.getSizeD(codigo);
	}
	@GetMapping("/dress/sub/{subcategoria}")
	public List<Dress> getSubcategoryD(@PathVariable String subcategoria){
		return dservice.getFromSubcategory(subcategoria);
	}
	@GetMapping("/dress/sex/{genero}")
	public List<Dress> getSexD(@PathVariable String genero){
		return dservice.getFromSex(genero);
	}
	@GetMapping("/dress/age/{edad}")
	public List<Dress> getAge(@PathVariable String edad){
		return dservice.getFromAge(edad);
	}
	@PostMapping("/dress/save")
	public void saveDress(@RequestBody Dress which){
		dservice.save(which);
	}
	@PutMapping("/dress/update")
	public void updateDress(@RequestBody Dress which){
		dservice.save(which);
	}

	//FRAGRANCE SECTOR
	@GetMapping("/fragrance/all")
	public List<Fragrance> getAllFragances(){
		return fservice.getAllFragrances();
	}
	@GetMapping("/fragrance/everything")
	public List<Fragrance> getEverythingOnFragrance(){
		return fservice.getEverything();
	}
	@GetMapping("fragrance/obj/{codigo}")
	public Fragrance getObjF(@PathVariable String codigo) {
		return fservice.getObjF(codigo);
	}
	@GetMapping("/fragrance/sub/{subcategoria}")
	public List<Fragrance> getSubcategoryF(@PathVariable String subcategoria){
		return fservice.getFromSubcategory(subcategoria);
	}
	@GetMapping("/fragrance/sex/{genero}")
	public List<Fragrance> getSexF(@PathVariable String genero){
		return fservice.getFromSex(genero);
	}
	@GetMapping("/fragrance/country/{pais}")
	public List<Fragrance> getOriginCountry(@PathVariable String pais){
		return fservice.getFromOriginCountry(pais);
	}
	@PostMapping("/fragrance/save")
	public void saveFragrance(@RequestBody Fragrance which){
		fservice.save(which);
	}
	@PutMapping("/fragrance/update")
	public void updateFragrance(@RequestBody Fragrance which){
		fservice.save(which);
	}

	//FAVORITOS SECTOR
	@PostMapping("/favoritos/add")
	public void saveFavorito(@RequestBody Favorito fav){
		favservice.add(fav);
	}
	@DeleteMapping("/favoritos/remove/{categoria}/{usuario}/{producto}")
	public void removeFavorito(
			@PathVariable String categoria,
			@PathVariable long usuario,
			@PathVariable long producto
	) {
		favservice.remove(categoria,usuario,producto);
	}
	@DeleteMapping("/favoritos/reset/{user}")
	public void resetValues(@PathVariable long user){
		List<Favorito> 	dressesFavs = favservice.getDressesByUserId(user),
						fragrancesFavs = favservice.getFragrancesByUserId(user);
		Optional<Dress> drRet = Optional.empty();
		Optional<Fragrance> frRet = Optional.empty();
		Dress dr = new Dress();
		Fragrance fr = new Fragrance();
		for(Favorito fav:dressesFavs){
			drRet = dservice.getFromId(fav.getIdProduct());
			if(drRet.isPresent()){
				dr = drRet.get();
				dr.setIsOnFavs(false);
				dservice.save(dr);
			}
		}
		for(Favorito fav:fragrancesFavs){
			frRet = fservice.getFromId(fav.getIdProduct());
			if(frRet.isPresent()){
				fr = frRet.get();
				fr.setIsOnFavs(false);
				fservice.save(fr);
				fservice.save(fr);
			}
		}
	}
	@PutMapping("/favoritos/restore")
	public void restoreFromUserId(@RequestBody long user){
		List<Favorito> 	dressesFavs = favservice.getDressesByUserId(user),
						fragrancesFavs = favservice.getFragrancesByUserId(user);
		Optional<Dress> drRet = Optional.empty();
		Optional<Fragrance> frRet = Optional.empty();
		Dress dr = new Dress();
		Fragrance fr = new Fragrance();
		for(Favorito fav:dressesFavs){
			drRet = dservice.getFromId(fav.getIdProduct());
			if(drRet.isPresent()){
				dr = drRet.get();
				dr.setIsOnFavs(true);
				dservice.save(dr);
			}
		}
		for(Favorito fav:fragrancesFavs){
			frRet = fservice.getFromId(fav.getIdProduct());
			if(frRet.isPresent()){
				fr = frRet.get();
				fr.setIsOnFavs(true);
				fservice.save(fr);
				fservice.save(fr);
			}
		}
	}
	@GetMapping("/favoritos/byUserId/{user}")
	public List<Product> getByUserId(@PathVariable long user){
		List<Product> ret = new ArrayList<Product>();
		List<Favorito> 	dressesFavs = favservice.getDressesByUserId(user),
						fragrancesFavs = favservice.getFragrancesByUserId(user);
		Optional<Dress> drRet = Optional.empty();
		Optional<Fragrance> frRet = Optional.empty();
		Dress dr = new Dress();
		Fragrance fr = new Fragrance();
		for(Favorito fav:dressesFavs) {
			drRet = dservice.getFromId(fav.getIdProduct());
			if(drRet.isPresent()) {
				dr = drRet.get();
				ret.add(dr);
			}
		}
		for(Favorito fav:fragrancesFavs){
			frRet = fservice.getFromId(fav.getIdProduct());
			if(frRet.isPresent()){
				fr = frRet.get();
				ret.add(fr);
			}
		}
		return ret;
	}
}