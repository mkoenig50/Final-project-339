//Super class for all movies, this will be the baseline.
class Movie
{
  Private boolean available;
  Private double cost = 3.00;
  Private String title;
  Movie(String newTitle)
  {
    available = true;
    title = newTitle;
  }

  Public boolean isAvailable()
  {
    return available;
  }

  Public void checkout()
  {
    available = false;
  }

  Public int rentalCost(int days)
  {
    if(days <= 3)
    {
      return cost * days;
    }
    else
    {
      return (cost * 3) + (cost * 1.5 * (days - 3));
    }
  }

  Public void returnMovie()
  {
    available = true;
  }
}
