from bs4 import BeautifulSoup
import urllib.request
import ssl

ssl._create_default_https_context = ssl._create_unverified_context

wiki = "https://www.pokemon.com/us/pokedex/"
page = urllib.request.urlopen(wiki) #Query the website
soup = BeautifulSoup(page,features="html.parser")   #Parse the html in the 'page' variable, and store it in Beautiful Soup format
all_ul = soup.find_all("a")
list_of_pokemon = []
pokemon_present = False

for link in all_ul:
    text = link.find_all(text=True)
    for t in text:
        if t == "1 - Bulbasaur" or pokemon_present == True:
            pokemon_present = True
            list_of_pokemon.append(t)
        else:
            break
        if t == "898 - Calyrex":
            pokemon_present = False
    if pokemon_present == False and list_of_pokemon != []:
        break
new_file = open("pokedex.txt", "w")
for line in list_of_pokemon:
  new_file.write(line)
  new_file.write("\n")
new_file.close()   
