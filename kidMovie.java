//The only difference betwee a kid's movie is the price.
class KidMovie extends Movie
{
  KidMovie(String newTitle)
  {
    super(newTitle);
    cost = 1.50;
  }
}
